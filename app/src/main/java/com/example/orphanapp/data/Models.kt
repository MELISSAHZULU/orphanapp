package com.example.orphanapp.data

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

// This file is the single source of truth for all data models in the app.

/**
 * Represents a registered user in the system.
 */
data class User(
    @DocumentId var uid: String = "",
    val email: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null
)

/**
 * Represents a single message within a conversation.
 */
data class ChatMessage(
    @DocumentId var id: String = "",
    val senderId: String = "",
    val text: String = "",
    @ServerTimestamp val timestamp: Date? = null
)

/**
 * Represents a single orphan. The `documentId` is the unique ID from Firestore.
 */
data class Orphan(
    @DocumentId var documentId: String = "",
    val name: String = "",
    val age: Int = 0,
    val dateOfBirth: Date? = null,
    val gender: String = "",
    val enrollmentDate: String = "",
    var status: String = "Pending", // e.g., Pending, Active, Inactive
    val photoUrl: String? = null,

    // Records
    val healthRecords: String = "",
    val orphanStory: String = "",
    val donationsReceived: List<String> = emptyList(),
    val sponsorInfo: String = "",
    val educationProgress: String = "",

    // Legacy/Compatibility fields
    val guardianName: String = "",
    val schoolName: String = ""
)


/**
 * Represents a single donation record. The `id` is the unique ID from Firestore.
 */
data class Donation(
    @DocumentId var id: String = "",
    val donorName: String = "",
    val amount: String = "",
    val date: String = ""
)

/**
 * Represents a single staff member. The `id` is the unique ID from Firestore.
 */
data class StaffMember(
    @DocumentId var id: String = "",
    val name: String = "",
    val role: String = "",
    val isActive: Boolean = true
)

/**
 * Represents a single item in the inventory. The `id` is the unique ID from Firestore.
 */
data class InventoryItem(
    @DocumentId var id: String = "",
    val name: String = "",
    val quantity: String = ""
)

/**
 * Represents a single activity log entry. The `id` is the unique ID from Firestore.
 */
data class ActivityLog(
    @DocumentId var id: String = "",
    val description: String = "",
    val timestamp: Date = Date()
)
