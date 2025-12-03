package com.example.orphanapp.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.orphanapp.data.User // Import the centralized User class
import com.example.orphanapp.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// The User data class has been moved to data/Models.kt

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
                    // Fetch the user document from Firestore to get custom fields like organizationId
                    val userDocument = repository.getUserDocument(firebaseUser.uid)
                    val organizationId = userDocument?.getString("organizationId")

                    // Create the User object using data from both Firebase Auth and Firestore
                    val user = User(
                        uid = firebaseUser.uid,
                        email = firebaseUser.email,
                        displayName = firebaseUser.displayName,
                        photoUrl = firebaseUser.photoUrl?.toString(),
                        organizationId = organizationId
                    )
                    _authState.value = AuthState.Authenticated(user)
                } else {
                    _authState.value = AuthState.Unauthenticated
                }
            }
        }
    }

    fun updateProfilePicture(uri: Uri) {
        viewModelScope.launch {
            try {
                repository.updateProfilePicture(uri)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Profile update failed")
            }
        }
    }

    fun updateUserDisplayName(displayName: String) {
        viewModelScope.launch {
            try {
                repository.updateUserDisplayName(displayName)
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Display name update failed")
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
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
