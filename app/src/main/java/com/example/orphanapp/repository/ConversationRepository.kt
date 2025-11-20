package com.example.orphanapp.repository

import com.example.orphanapp.data.ChatMessage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.snapshots // <<< THE DEFINITIVE, FINAL FIX
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

interface ConversationRepository {
    fun getMessages(conversationId: String): Flow<List<ChatMessage>>
    suspend fun sendMessage(conversationId: String, message: ChatMessage)
}

class ConversationRepositoryImpl : ConversationRepository {
    private val firestore = FirebaseFirestore.getInstance()

    override fun getMessages(conversationId: String): Flow<List<ChatMessage>> {
        return firestore
            .collection("conversations")
            .document(conversationId)
            .collection("messages")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .snapshots() // This will now resolve correctly
            .map { snapshot: QuerySnapshot -> // Explicitly declare the type here
                snapshot.toObjects(ChatMessage::class.java)
            }
    }

    override suspend fun sendMessage(conversationId: String, message: ChatMessage) {
        firestore
            .collection("conversations")
            .document(conversationId)
            .collection("messages")
            .add(message)
            .await()
    }
}
