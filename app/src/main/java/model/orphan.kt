package com.example.orphanapp.model

import com.google.firebase.firestore.DocumentId
import java.util.Date

data class ActivityLog(
    val id: Int? = null,
    val date: Date = Date(),
    val activity: String = "",
    val notes: String = ""
)

data class Guardian(
    val name: String = "",
    val relation: String = "",
    val contact: String = ""
)

data class Orphan(
    @DocumentId val documentId: String = "",
    val id: Int = 0,
    val name: String = "",
    val age: Int = 0,
    val gender: String = "",
    val guardianName: String = "",
    val schoolName: String = "",
    var status: String = "Active",
    val photoUrl: String? = null,
    val guardians: MutableList<Guardian> = mutableListOf(),
    val activityLogs: MutableList<ActivityLog> = mutableListOf()
)
