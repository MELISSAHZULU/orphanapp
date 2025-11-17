package com.example.orphanapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class ChatMessage(val message: String, val isFromMe: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationScreen(navController: NavController, contactName: String?) {
    var messageText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf(
        ChatMessage("Hello!", false),
        ChatMessage("Hi there! How are you?", true),
        ChatMessage("I'm doing well, thanks for asking!", false)
    ) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(contactName ?: "Chat") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } },
                 colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Type a message...") },
                        shape = RoundedCornerShape(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = {
                        if (messageText.isNotBlank()) {
                            messages.add(ChatMessage(messageText, true))
                            messageText = ""
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(12.dp),
            reverseLayout = true
        ) {
            items(messages.reversed()) { chatMessage ->
                MessageBubble(chatMessage)
            }
        }
    }
}

@Composable
fun MessageBubble(chatMessage: ChatMessage) {
    val horizontalArrangement = if (chatMessage.isFromMe) Arrangement.End else Arrangement.Start
    val bubbleColor = if (chatMessage.isFromMe) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(bubbleColor)
                .padding(12.dp)
        ) {
            Text(text = chatMessage.message)
        }
    }
}
