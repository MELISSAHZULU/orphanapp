package com.example.orphanapp.repository

import android.util.Log
import com.example.orphanapp.data.StaffMember // This now correctly refers to the class in Models.kt
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

interface StaffRepository {
    fun getStaff(): Flow<List<StaffMember>>
    suspend fun addStaff(staffMember: StaffMember)
    suspend fun updateStaff(staffMember: StaffMember)
}

class StaffRepositoryImpl : StaffRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val staffCollection = firestore.collection("staff")

    override fun getStaff(): Flow<List<StaffMember>> = callbackFlow {
        val listenerRegistration = staffCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w("StaffRepository", "Listen error", error)
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val staff = snapshot.documents.mapNotNull { document ->
                    try {
                        val staffMember = document.toObject(StaffMember::class.java)
                        staffMember?.copy(id = document.id)
                    } catch (e: Exception) {
                        Log.e("StaffRepository", "Error converting document", e)
                        null
                    }
                }
                trySend(staff)
            } else {
                trySend(emptyList())
            }
        }
        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun addStaff(staffMember: StaffMember) {
        staffCollection.add(staffMember).await()
    }

    override suspend fun updateStaff(staffMember: StaffMember) {
        if (staffMember.id.isNotEmpty()) {
            staffCollection.document(staffMember.id).set(staffMember).await()
        } else {
            Log.w("StaffRepository", "Cannot update staff without a document ID.")
        }
    }
}
