package com.example.orphanapp

import android.app.Application
import com.example.orphanapp.repository.AuthRepository
import com.example.orphanapp.repository.OrphanRepository
import com.example.orphanapp.repository.OrphanRepositoryImpl
import com.google.firebase.FirebaseApp

class OrphanApplication : Application() {

    lateinit var orphanRepository: OrphanRepository
    lateinit var authRepository: AuthRepository

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        orphanRepository = OrphanRepositoryImpl()
        authRepository = AuthRepository()
    }
}
