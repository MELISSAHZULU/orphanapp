package com.example.orphanapp.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

// This is a fake repository for testing purposes and does not interact with Firebase.
class FakeAuthRepository : AuthRepository {

    private val _authState = MutableStateFlow<FirebaseUser?>(null)
    override val authState: Flow<FirebaseUser?> = _authState

    override suspend fun signIn(email: String, password: String): FirebaseUser? {
        // Not implemented for fake repository
        return null
    }

    override suspend fun register(email: String, password: String, displayName: String): FirebaseUser? {
        // Not implemented for fake repository
        return null
    }

    override suspend fun updateProfilePicture(uri: Uri) {
        // Not implemented for fake repository
    }

    override suspend fun updateUserDisplayName(displayName: String) {
        // Not implemented for fake repository
    }

    override fun signOut() {
        _authState.value = null
    }
}
