package com.example.orphanapp.repository

import android.util.Log
import com.example.orphanapp.data.Donation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

interface DonationRepository {
    fun getDonations(): Flow<List<Donation>>
    suspend fun addDonation(donation: Donation)
}

class DonationRepositoryImpl : DonationRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val donationsCollection = firestore.collection("donations")

    override fun getDonations(): Flow<List<Donation>> = callbackFlow {
        val listenerRegistration = donationsCollection
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w("DonationRepository", "Listen error", error)
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val donations = snapshot.documents.mapNotNull { document ->
                        try {
                            val donation = document.toObject(Donation::class.java)
                            donation?.copy(id = document.id)
                        } catch (e: Exception) {
                            Log.e("DonationRepository", "Error converting document", e)
                            null
                        }
                    }
                    trySend(donations)
                } else {
                    trySend(emptyList())
                }
            }
        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun addDonation(donation: Donation) {
        donationsCollection.add(donation).await()
    }
}
