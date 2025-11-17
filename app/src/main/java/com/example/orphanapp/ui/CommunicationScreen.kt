package com.example.orphanapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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

data class Contact(val id: Int, val name: String, val lastMessage: String, val timestamp: String, val avatar: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunicationScreen(navController: NavController) {
    // Dummy data for contacts
    val contacts = listOf(
        Contact(1, "John Doe", "See you tomorrow!", "10:42 AM", R.drawable.ic_launcher_foreground),
        Contact(2, "Jane Smith", "Thanks for the update.", "Yesterday", R.drawable.ic_launcher_foreground),
        Contact(3, "Peter Jones", "Can we reschedule?", "Tuesday", R.drawable.ic_launcher_foreground)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Communication") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("conversation/New Chat") }) {
                Icon(Icons.Default.Add, contentDescription = "New Conversation")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(contacts) { contact ->
                ContactListItem(contact = contact) {
                    navController.navigate("conversation/${contact.name}")
                }
            }
        }
    }
}

@Composable
fun ContactListItem(contact: Contact, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = contact.avatar),
            contentDescription = "${contact.name} Avatar",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = contact.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(text = contact.lastMessage, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
        Text(text = contact.timestamp, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}
