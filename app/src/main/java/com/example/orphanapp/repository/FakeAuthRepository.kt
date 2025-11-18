package com.example.orphanapp.repository

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

// This is a fake repository for testing purposes and does not interact with Firebase.
class FakeAuthRepository : AuthRepository {

    // We don't have a good way to fake a FirebaseUser, so we'll just keep this simple.
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

    override fun signOut() {
        _authState.value = null
    }
}
