package com.example.orphanapp.repository

import android.util.Log
import com.example.orphanapp.data.Orphan
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

interface OrphanRepository {
    fun getOrphans(): Flow<List<Orphan>>
    suspend fun addOrphan(orphan: Orphan): String // Return the new document ID
    suspend fun updateOrphan(orphan: Orphan)
    suspend fun updateOrphanStatus(orphanId: String, newStatus: String)
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
            if (snapshot != null) {
                try {
                    val orphans = snapshot.toObjects(Orphan::class.java)
                    trySend(orphans)
                } catch (e: Exception) {
                    Log.e("OrphanRepository", "Error converting snapshot to objects", e)
                    close(e)
                }
            } else {
                 trySend(emptyList())
            }
        }
        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun addOrphan(orphan: Orphan): String {
        val documentReference = orphansCollection.add(orphan).await()
        return documentReference.id
    }

    override suspend fun updateOrphan(orphan: Orphan) {
        if (orphan.documentId.isNotEmpty()) {
            orphansCollection.document(orphan.documentId).set(orphan).await()
        } else {
            Log.w("OrphanRepository", "Cannot update orphan without a document ID.")
        }
    }

    override suspend fun updateOrphanStatus(orphanId: String, newStatus: String) {
        if (orphanId.isNotEmpty()) {
            orphansCollection.document(orphanId).update("status", newStatus).await()
        } else {
             Log.w("OrphanRepository", "Cannot update orphan status without a document ID.")
        }
    }
}
