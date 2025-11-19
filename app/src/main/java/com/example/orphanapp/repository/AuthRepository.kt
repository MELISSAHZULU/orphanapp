package com.example.orphanapp.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

interface AuthRepository {
    val authState: Flow<FirebaseUser?>
    suspend fun signIn(email: String, password: String): FirebaseUser?
    suspend fun register(email: String, password: String, displayName: String): FirebaseUser?
    suspend fun updateProfilePicture(uri: Uri)
    suspend fun updateUserDisplayName(displayName: String)
    fun signOut()
}

class AuthRepositoryImpl : AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

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
        return firebaseAuth.currentUser
    }

    override suspend fun updateProfilePicture(uri: Uri) {
        val user = firebaseAuth.currentUser ?: throw IllegalStateException("User not logged in")
        val storageRef = storage.reference.child("profile_pictures/${user.uid}")

        val uploadTask = storageRef.putFile(uri).await()
        val downloadUrl = uploadTask.storage.downloadUrl.await()

        val profileUpdates = userProfileChangeRequest {
            photoUri = downloadUrl
        }
        user.updateProfile(profileUpdates).await()
    }

    override suspend fun updateUserDisplayName(displayName: String) {
        val user = firebaseAuth.currentUser ?: throw IllegalStateException("User not logged in")
        val profileUpdates = userProfileChangeRequest {
            this.displayName = displayName
        }
        user.updateProfile(profileUpdates).await()
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}
