package com.example.orphanapp

import android.app.Application
import com.example.orphanapp.repository.AuthRepository
import com.example.orphanapp.repository.AuthRepositoryImpl
import com.example.orphanapp.repository.DonationRepository
import com.example.orphanapp.repository.DonationRepositoryImpl
import com.example.orphanapp.repository.OrphanRepository
import com.example.orphanapp.repository.OrphanRepositoryImpl
import com.example.orphanapp.repository.StaffRepository
import com.example.orphanapp.repository.StaffRepositoryImpl
import com.google.firebase.FirebaseApp

class OrphanApplication : Application() {

    lateinit var orphanRepository: OrphanRepository
    lateinit var authRepository: AuthRepository
    lateinit var donationRepository: DonationRepository
    lateinit var staffRepository: StaffRepository

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        orphanRepository = OrphanRepositoryImpl()
        authRepository = AuthRepositoryImpl()
        donationRepository = DonationRepositoryImpl()
        staffRepository = StaffRepositoryImpl()
    }
}
