package com.example.orphanapp.repository

import com.example.orphanapp.data.ActivityLog
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

interface ActivityLogRepository {
    fun getActivityLogs(): Flow<List<ActivityLog>>
    suspend fun addActivityLog(description: String)
}

class ActivityLogRepositoryImpl : ActivityLogRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val activityLogCollection = firestore.collection("activity_logs")

    override fun getActivityLogs(): Flow<List<ActivityLog>> {
        return activityLogCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .snapshots()
            .map { snapshot ->
                snapshot.toObjects(ActivityLog::class.java)
            }
    }

    override suspend fun addActivityLog(description: String) {
        val log = ActivityLog(description = description)
        activityLogCollection.add(log).await()
    }
}
