package com.example.orphanapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class ActivityLog(val id: Int, val description: String, val date: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityLogScreen(navController: NavController) {
    val activities = listOf(
        ActivityLog(1, "John Doe visited the orphanage.", "2024-05-20"),
        ActivityLog(2, "New donation of clothes received.", "2024-05-19"),
        ActivityLog(3, "Christmas party organized.", "2023-12-25"),
        ActivityLog(4, "Medical check-up for all children.", "2023-11-15"),
        ActivityLog(5, "Visit to the national park.", "2023-10-01")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Activity Log") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(activities) { activity ->
                ActivityLogItem(log = activity)
            }
        }
    }
}

@Composable
fun ActivityLogItem(log: ActivityLog) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = log.description, style = MaterialTheme.typography.bodyLarge)
            Text(text = log.date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
