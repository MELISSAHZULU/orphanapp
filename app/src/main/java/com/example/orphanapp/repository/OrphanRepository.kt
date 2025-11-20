package com.example.orphanapp.repository

import com.example.orphanapp.data.Orphan
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

interface OrphanRepository {
    fun getOrphans(): Flow<List<Orphan>>
    suspend fun getOrphanById(orphanId: String): Orphan?
    suspend fun addOrphan(orphan: Orphan): String?
    suspend fun updateOrphan(orphan: Orphan)
    suspend fun updateOrphanStatus(orphanId: String, newStatus: String)
}

class OrphanRepositoryImpl : OrphanRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val orphansCollection = firestore.collection("orphans")

    override fun getOrphans(): Flow<List<Orphan>> {
        return orphansCollection.snapshots().map { snapshot ->
            snapshot.documents.mapNotNull { document ->
                val orphan = document.toObject<Orphan>()
                orphan?.apply { documentId = document.id }
            }
        }
    }

    override suspend fun getOrphanById(orphanId: String): Orphan? {
        return try {
            val document = orphansCollection.document(orphanId).get().await()
            document.toObject<Orphan>()?.apply { documentId = document.id }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun addOrphan(orphan: Orphan): String? {
        return try {
            val documentReference = orphansCollection.add(orphan).await()
            documentReference.id
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateOrphan(orphan: Orphan) {
        orphansCollection.document(orphan.documentId).set(orphan).await()
    }

    override suspend fun updateOrphanStatus(orphanId: String, newStatus: String) {
        orphansCollection.document(orphanId).update("status", newStatus).await()
    }
}