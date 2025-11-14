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
    val birthCertificate: Boolean = false,
    val deathCertificate: Boolean = false,
    val guardianConsent: Boolean = false,
    val healthRecord: Boolean = false,
    val ageInRange: Boolean = false,
    val recommendation: Boolean = false,
    val reasonForAdmission: Boolean = false,
    val orphanStatusProvided: Boolean = false,
    val healthInfoProvided: Boolean = false,
    val storySummaryProvided: Boolean = false,
    var status: String = "Active",
    val photoUrl: String? = null,
    val guardians: MutableList<Guardian> = mutableListOf(),
    val activityLogs: MutableList<ActivityLog> = mutableListOf()
)
