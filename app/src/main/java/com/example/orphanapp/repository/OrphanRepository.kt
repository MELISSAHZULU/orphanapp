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
            if (snapshot != null) {
                // Manually map documents to include the document ID
                val orphans = snapshot.documents.mapNotNull { document ->
                    try {
                        val orphan = document.toObject(Orphan::class.java)
                        orphan?.copy(documentId = document.id)
                    } catch (e: Exception) {
                        Log.e("OrphanRepository", "Error converting document", e)
                        null
                    }
                }
                trySend(orphans)
            } else {
                 trySend(emptyList())
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
        } else {
            Log.w("OrphanRepository", "Cannot update orphan without a document ID.")
        }
    }
}
