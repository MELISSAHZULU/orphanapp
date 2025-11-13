package com.example.orphanapp.repository

import android.util.Log
import com.example.orphanapp.model.Orphan
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

interface OrphanRepository {
    fun getOrphans(): Flow<List<Orphan>>
    suspend fun addOrphan(orphan: Orphan)
    suspend fun updateOrphan(orphan: Orphan)
}

class OrphanRepositoryImpl : OrphanRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val orphansCollection = firestore.collection("orphans")

    override fun getOrphans(): Flow<List<Orphan>> = callbackFlow {
        val listenerRegistration = orphansCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w("OrphanRepository", "Listen error", error)
                close(error)
                return@addSnapshotListener
            }
            try {
                if (snapshot != null) {
                    val orphans = snapshot.toObjects(Orphan::class.java)
                    trySend(orphans).isSuccess
                }
            } catch (e: Exception) {
                Log.e("OrphanRepository", "Error converting snapshot to objects", e)
                close(e) // Close the flow on error
            }
        }
        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun addOrphan(orphan: Orphan) {
        orphansCollection.add(orphan).await()
    }

    override suspend fun updateOrphan(orphan: Orphan) {
        if (orphan.documentId.isNotEmpty()) {
            orphansCollection.document(orphan.documentId).set(orphan).await()
        }
    }
}
