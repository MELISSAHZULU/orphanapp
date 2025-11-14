package com.example.orphanapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.orphanapp.R

data class Message(val id: Int, val sender: String, val lastMessage: String, val timestamp: String, val avatar: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunicationScreen(navController: NavController) {
    val messages = listOf(
        Message(1, "John", "How are you?", "8:45 AM", R.drawable.ic_launcher_background),
        Message(2, "Anna", "Sure, I can help!", "Yesterday", R.drawable.ic_launcher_background),
        Message(3, "Mark", "I'll be there soon", "Tuesday", R.drawable.ic_launcher_background),
        Message(4, "Sara", "Thank you!", "Monday", R.drawable.ic_launcher_background)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Communication", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Button(
                onClick = { /* TODO: Start new conversation */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surface, contentColor = MaterialTheme.colorScheme.onSurface)
            ) {
                Text("Start a new conversation", modifier = Modifier.padding(8.dp))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Messages", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(messages) { message ->
                    MessageListItem(message = message)
                }
            }
        }
    }
}

@Composable
fun MessageListItem(message: Message) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = message.avatar),
                contentDescription = message.sender,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(message.sender, fontWeight = FontWeight.Bold)
                Text(message.lastMessage, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(message.timestamp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
