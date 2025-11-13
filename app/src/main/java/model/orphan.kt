package com.example.orphanapp.model

import java.util.Date

data class ActivityLog(
    val id: Int,
    val date: Date,
    val activity: String,
    val notes: String
)

data class Orphan(
    val id: Int,
    val name: String,
    val age: Int,
    val gender: String,
    val birthCertificate: Boolean,
    var status: String = "Active",
    val activityLogs: MutableList<ActivityLog> = mutableListOf()
)
