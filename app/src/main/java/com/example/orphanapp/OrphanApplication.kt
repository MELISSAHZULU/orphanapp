package com.example.orphanapp

import android.app.Application
import com.example.orphanapp.repository.*
import com.google.firebase.FirebaseApp

class OrphanApplication : Application() {

    lateinit var orphanRepository: OrphanRepository
    lateinit var authRepository: AuthRepository
    lateinit var donationRepository: DonationRepository
    lateinit var staffRepository: StaffRepository
    lateinit var inventoryRepository: InventoryRepository

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        orphanRepository = OrphanRepositoryImpl()
        authRepository = AuthRepositoryImpl()
        donationRepository = DonationRepositoryImpl()
        staffRepository = StaffRepositoryImpl()
        inventoryRepository = InventoryRepositoryImpl()
    }
}
