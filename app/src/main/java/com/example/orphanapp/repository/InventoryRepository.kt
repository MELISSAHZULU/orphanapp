package com.example.orphanapp.repository

import android.util.Log
import com.example.orphanapp.data.InventoryItem
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

interface InventoryRepository {
    fun getInventory(): Flow<List<InventoryItem>>
    suspend fun addInventoryItem(item: InventoryItem)
    suspend fun updateInventoryItem(item: InventoryItem)
}

class InventoryRepositoryImpl : InventoryRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val inventoryCollection = firestore.collection("inventory")

    override fun getInventory(): Flow<List<InventoryItem>> = callbackFlow {
        val listenerRegistration = inventoryCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w("InventoryRepository", "Listen error", error)
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                val items = snapshot.documents.mapNotNull { document ->
                    try {
                        val item = document.toObject(InventoryItem::class.java)
                        item?.copy(id = document.id)
                    } catch (e: Exception) {
                        Log.e("InventoryRepository", "Error converting document", e)
                        null
                    }
                }
                trySend(items)
            } else {
                trySend(emptyList())
            }
        }
        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun addInventoryItem(item: InventoryItem) {
        inventoryCollection.add(item).await()
    }

    override suspend fun updateInventoryItem(item: InventoryItem) {
        if (item.id.isNotEmpty()) {
            inventoryCollection.document(item.id).set(item).await()
        } else {
            Log.w("InventoryRepository", "Cannot update item without a document ID.")
        }
    }
}
