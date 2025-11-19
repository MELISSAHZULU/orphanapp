package com.example.orphanapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.orphanapp.data.ChatMessage
import com.example.orphanapp.repository.ConversationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ConversationViewModel(private val repository: ConversationRepository, private val currentUserId: String) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    fun loadMessages(conversationId: String) {
        viewModelScope.launch {
            repository.getMessages(conversationId)
                .catch { e ->
                    // Handle error, e.g., post to an error state flow
                    println("Error loading messages: ${e.message}")
                }
                .collect { messageList ->
                    _messages.value = messageList
                }
        }
    }

    fun sendMessage(conversationId: String, text: String) {
        if (text.isBlank()) return

        val message = ChatMessage(
            senderId = currentUserId,
            text = text
            // timestamp is handled by the server
        )

        viewModelScope.launch {
            try {
                repository.sendMessage(conversationId, message)
            } catch (e: Exception) {
                // Handle error
                 println("Error sending message: ${e.message}")
            }
        }
    }
}

class ConversationViewModelFactory(
    private val repository: ConversationRepository,
    private val currentUserId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConversationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ConversationViewModel(repository, currentUserId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
