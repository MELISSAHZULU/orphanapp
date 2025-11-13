package com.example.orphanapp.repository

import com.example.orphanapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

interface AuthRepository {
    suspend fun signIn(email: String, password: String): User?
    suspend fun register(email: String, password: String): User?
    fun getCurrentUser(): User?
    fun signOut()
}

class AuthRepositoryImpl : AuthRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()

    private fun FirebaseUser.toUser(): User = User(uid = this.uid, email = this.email)

    override suspend fun signIn(email: String, password: String): User? {
        return firebaseAuth.signInWithEmailAndPassword(email, password).await().user?.toUser()
    }

    override suspend fun register(email: String, password: String): User? {
        return firebaseAuth.createUserWithEmailAndPassword(email, password).await().user?.toUser()
    }

    override fun getCurrentUser(): User? {
        return firebaseAuth.currentUser?.toUser()
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }
}
