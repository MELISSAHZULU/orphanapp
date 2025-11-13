package com.example.orphanapp.repository

import com.example.orphanapp.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeAuthRepository : AuthRepository {

    private val users = mutableListOf(
        User(uid = "1", email = "test@example.com"),
        User(uid = "2", email = "user@example.com")
    )

    private val _currentUser = MutableStateFlow<User?>(null)
    private fun setCurrentUser(user: User?) {
        _currentUser.value = user
    }

    override suspend fun signIn(email: String, password: String): User? {
        val user = users.find { it.email == email }
        setCurrentUser(user)
        return user
    }

    override suspend fun register(email: String, password: String): User? {
        val newUser = User(uid = (users.size + 1).toString(), email = email)
        users.add(newUser)
        setCurrentUser(newUser)
        return newUser
    }

    override fun getCurrentUser(): User? {
        return _currentUser.value
    }

    override fun signOut() {
        setCurrentUser(null)
    }
}
