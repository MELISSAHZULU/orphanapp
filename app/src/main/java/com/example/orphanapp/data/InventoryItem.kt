package com.example.orphanapp.data

/**
 * Represents a single item in the inventory.
 * The `id` is a String to match the document ID from Firestore.
 */
data class InventoryItem(
    val id: String = "",
    val name: String = "",
    val quantity: String = ""
)
