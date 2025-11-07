package com.example.orphanapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.orphanapp.model.Orphan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrphanProfileScreen(navController: NavController, orphan: Orphan?) {
    var status by remember { mutableStateOf(orphan?.status ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Orphan Profile: ${orphan?.name ?: ""}") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController, "profile")
        }
    ) { padding ->
        if (orphan != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Basic Info Card
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Name: ${orphan.name}", style = MaterialTheme.typography.bodyLarge)
                        Text("Age: ${orphan.age}", style = MaterialTheme.typography.bodyLarge)
                        Text("Gender: ${orphan.gender}", style = MaterialTheme.typography.bodyLarge)
                        Text("Enrollment Date: 2025-10-10", style = MaterialTheme.typography.bodyLarge) // Placeholder
                        Text("Status: $status", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                // Records Section
                Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Records", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        RecordItem(label = "Health Records", action = "View/Edit")
                        RecordItem(label = "Education Progress", action = "View/Edit")
                        RecordItem(label = "Donations Received", action = "View")
                        RecordItem(label = "Sponsor Info", action = "View/Edit")
                        RecordItem(label = "Adoption Requests", action = "View")
                        RecordItem(label = "Activity Logs", action = "View/Add")
                    }
                }
                Spacer(modifier = Modifier.weight(1f))

                // Action Buttons
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(onClick = {
                        orphan.status = "Updated"
                        status = "Updated"
                    }) {
                        Text("Update Status")
                    }
                    OutlinedButton(onClick = { navController.navigate("tracking") }) {
                        Text("Back to List", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        } else {
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                Text("Orphan not found. Please enroll an orphan first.")
            }
        }
    }
}

@Composable
fun RecordItem(label: String, action: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.weight(1f))
        Text("[$action]", color = MaterialTheme.colorScheme.secondary)
    }
}
