package com.example.orphanapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orphanapp.ui.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ConversationViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    fun loadConversation(conversationId: String) {
        viewModelScope.launch {
            // In a real app, you would load this from a repository based on the conversationId
            if (conversationId != "New Chat") {
                // For now, we just log it. Replace with actual data loading.
                println("Loading conversation: $conversationId")
            } else {
                _messages.value = emptyList()
            }
        }
    }

    fun sendMessage(message: String) {
        if (message.isNotBlank()) {
            val newMessage = ChatMessage(message, true) // Assume sender is always "me"
            _messages.value = _messages.value + newMessage
            // In a real app, you would save this to your repository
        }
    }
}
