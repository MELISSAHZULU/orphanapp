package com.example.orphanapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.orphanapp.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Define the User data class locally within this file to avoid conflicts.
// This is the single source of truth for the UI's User object.
data class User(val uid: String, val email: String?, val displayName: String?, val organizationId: String?)

sealed class AuthState {
    object Loading : AuthState()
    data class Authenticated(val user: User) : AuthState()
    object Unauthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState: StateFlow<AuthState> = _authState

    init {
        viewModelScope.launch {
            repository.authState.collect { firebaseUser ->
                if (firebaseUser != null) {
                    val userDocument = repository.getUserDocument(firebaseUser.uid)
                    val organizationId = userDocument?.getString("organizationId")
                    // Map the official FirebaseUser to our local UI-specific User data class
                    val user = User(firebaseUser.uid, firebaseUser.email, firebaseUser.displayName, organizationId)
                    _authState.value = AuthState.Authenticated(user)
                } else {
                    _authState.value = AuthState.Unauthenticated
                }
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                // The repository now returns a FirebaseUser, which we handle in the init collector.
                repository.signIn(email, password)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Sign in failed", e)
                _authState.value = AuthState.Error(e.message ?: "Sign in failed")
            }
        }
    }

    fun register(email: String, password: String, displayName: String) {
        viewModelScope.launch {
            try {
                // The repository now returns a FirebaseUser, which we handle in the init collector.
                repository.register(email, password, displayName)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Registration failed", e)
                _authState.value = AuthState.Error(e.message ?: "Registration failed")
            }
        }
    }

    fun signOut() {
        repository.signOut()
        _authState.value = AuthState.Unauthenticated
    }
}

class AuthViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
