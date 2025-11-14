package com.example.orphanapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.orphanapp.ui.theme.OrphanAppTheme

data class Message(val from: String, val content: String)
data class Announcement(val title: String, val date: String)

@Composable
fun CommunicationScreen(navController: NavController) {
    val messages = listOf(
        Message("Alice", "Can you please check on the new supplies?"),
        Message("Bob", "Sure, I will do it right away.")
    )

    val announcements = listOf(
        Announcement("Christmas Party", "25th December 2024"),
        Announcement("Annual General Meeting", "1st January 2025")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text("Communication", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Secure Messaging", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(messages) { message ->
                        Text("${message.from}: ${message.content}", modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Announcement Board", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(announcements) { announcement ->
                        Text("${announcement.title} - ${announcement.date}", modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommunicationScreenPreview() {
    OrphanAppTheme {
        CommunicationScreen(rememberNavController())
    }
}
