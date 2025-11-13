package com.example.orphanapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.orphanapp.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _user = MutableStateFlow<FirebaseUser?>(repository.getCurrentUser())
    val user: StateFlow<FirebaseUser?> = _user

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                _user.value = repository.signIn(email, password)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Sign in failed", e)
                // Optionally, you can expose an error state to the UI
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            try {
                _user.value = repository.register(email, password)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Registration failed", e)
                // Optionally, you can expose an error state to the UI
            }
        }
    }

    fun signOut() {
        repository.signOut()
        _user.value = null
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
