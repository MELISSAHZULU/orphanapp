package com.example.orphanapp.repository

import android.net.Uri
import com.example.orphanapp.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

interface AuthRepository {
    val authState: Flow<FirebaseUser?>
    fun getAllUsers(): Flow<List<User>>
    suspend fun signIn(email: String, password: String): FirebaseUser?
    suspend fun register(email: String, password: String, displayName: String): FirebaseUser?
    suspend fun updateProfilePicture(uri: Uri)
    suspend fun updateUserDisplayName(displayName: String)
    fun signOut()
}

class AuthRepositoryImpl : AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val usersCollection = firestore.collection("users")

    override val authState: Flow<FirebaseUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose { firebaseAuth.removeAuthStateListener(authStateListener) }
    }

    override fun getAllUsers(): Flow<List<User>> {
        return usersCollection.snapshots().map { snapshot ->
            snapshot.toObjects(User::class.java)
        }
    }

    override suspend fun signIn(email: String, password: String): FirebaseUser? {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await().user
    }

    override suspend fun register(email: String, password: String, displayName: String): FirebaseUser? {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val firebaseUser = authResult.user
        if (firebaseUser != null) {
            // Update the Firebase Auth profile
            val profileUpdates = userProfileChangeRequest {
                this.displayName = displayName
            }
            firebaseUser.updateProfile(profileUpdates).await()

            // Also create a user document in the 'users' collection in Firestore
            val user = User(
                uid = firebaseUser.uid,
                email = firebaseUser.email,
                displayName = displayName
            )
            usersCollection.document(firebaseUser.uid).set(user).await()
        }
        return firebaseUser
    }

    override suspend fun updateProfilePicture(uri: Uri) {
        val user = firebaseAuth.currentUser ?: throw IllegalStateException("User not logged in")
        val storageRef = storage.reference.child("profile_pictures/${user.uid}")

        val uploadTask = storageRef.putFile(uri).await()
        val downloadUrl = uploadTask.storage.downloadUrl.await()

        // Update Auth profile
        val profileUpdates = userProfileChangeRequest {
            photoUri = downloadUrl
        }
        user.updateProfile(profileUpdates).await()

        // Update Firestore user document
        usersCollection.document(user.uid).update("photoUrl", downloadUrl.toString()).await()
    }

    override suspend fun updateUserDisplayName(displayName: String) {
        val user = firebaseAuth.currentUser ?: throw IllegalStateException("User not logged in")
        // Update Auth profile
        val profileUpdates = userProfileChangeRequest {
            this.displayName = displayName
        }
        user.updateProfile(profileUpdates).await()

        // Update Firestore user document
        usersCollection.document(user.uid).update("displayName", displayName).await()
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}
