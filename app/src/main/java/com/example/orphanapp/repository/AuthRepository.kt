package com.example.orphanapp.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

interface AuthRepository {
    val authState: Flow<FirebaseUser?>
    suspend fun signIn(email: String, password: String): FirebaseUser?
    suspend fun register(email: String, password: String, displayName: String): FirebaseUser?
    fun signOut()
    suspend fun getUserDocument(uid: String): DocumentSnapshot?
    fun getAllUsers(): Flow<List<com.example.orphanapp.data.User>>
    suspend fun updateUserDisplayName(displayName: String)
    suspend fun updateProfilePicture(uri: Uri)
}

class AuthRepositoryImpl : AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override val authState: Flow<FirebaseUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        awaitClose { firebaseAuth.removeAuthStateListener(authStateListener) }
    }

    override suspend fun signIn(email: String, password: String): FirebaseUser? {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await().user
    }

    override suspend fun register(email: String, password: String, displayName: String): FirebaseUser? {
        val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
        val user = authResult.user
        if (user != null) {
            val profileUpdates = userProfileChangeRequest {
                this.displayName = displayName
            }
            user.updateProfile(profileUpdates).await()
        }
        // Return the user with the updated profile
        return firebaseAuth.currentUser
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override suspend fun getUserDocument(uid: String): DocumentSnapshot? {
        return firestore.collection("users").document(uid).get().await()
    }

    override fun getAllUsers(): Flow<List<com.example.orphanapp.data.User>> = callbackFlow {
        val listenerRegistration = firestore.collection("users")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val users = snapshot.toObjects(com.example.orphanapp.data.User::class.java)
                    trySend(users)
                } else {
                    trySend(emptyList())
                }
            }
        awaitClose { listenerRegistration.remove() }
    }
    override suspend fun updateUserDisplayName(displayName: String) {
        val user = firebaseAuth.currentUser
        if (user != null) {
            val profileUpdates = userProfileChangeRequest {
                this.displayName = displayName
            }
            user.updateProfile(profileUpdates).await()
        }
    }

    override suspend fun updateProfilePicture(uri: Uri) {
        val user = firebaseAuth.currentUser
        if (user != null) {
            val profileUpdates = userProfileChangeRequest {
                this.photoUri = uri
            }
            user.updateProfile(profileUpdates).await()
        }
    }
}
