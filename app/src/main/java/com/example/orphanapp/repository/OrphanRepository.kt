package com.example.orphanapp.repository

import android.util.Log
import com.example.orphanapp.data.Orphan // This now correctly refers to the class in Models.kt
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

interface OrphanRepository {
    fun getOrphans(organizationId: String): Flow<List<Orphan>>
    suspend fun addOrphan(orphan: Orphan): String
    suspend fun updateOrphan(orphan: Orphan)
    suspend fun getOrphanById(orphanId: String): Orphan?
    suspend fun updateOrphanStatus(orphanId: String, status: String)
}

class OrphanRepositoryImpl : OrphanRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val orphansCollection = firestore.collection("orphans")

    override fun getOrphans(organizationId: String): Flow<List<Orphan>> = callbackFlow {
        val listenerRegistration = orphansCollection
            .whereEqualTo("organizationId", organizationId)
            .addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w("OrphanRepository", "Listen error", error)
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                try {
                    // toObjects() automatically maps the documents to our data class.
                    // The @DocumentId annotation in the Orphan class handles the ID.
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
        // Firestore will automatically generate an ID for the new document.
        val documentReference = orphansCollection.add(orphan).await()
        return documentReference.id
    }

    override suspend fun updateOrphan(orphan: Orphan) {
        // We must have a documentId to update an existing orphan record.
        if (orphan.documentId.isNotEmpty()) {
            orphansCollection.document(orphan.documentId).set(orphan).await()
        } else {
            Log.w("OrphanRepository", "Cannot update orphan without a document ID.")
        }
    }

    override suspend fun getOrphanById(orphanId: String): Orphan? {
        return try {
            val snapshot = orphansCollection.document(orphanId).get().await()
            snapshot.toObject(Orphan::class.java)
        } catch (e: Exception) {
            Log.e("OrphanRepository", "Error getting orphan by ID", e)
            null
        }
    }

    override suspend fun updateOrphanStatus(orphanId: String, status: String) {
        try {
            orphansCollection.document(orphanId).update("status", status).await()
        } catch (e: Exception) {
            Log.e("OrphanRepository", "Error updating orphan status", e)
            throw e
        }
    }
}
