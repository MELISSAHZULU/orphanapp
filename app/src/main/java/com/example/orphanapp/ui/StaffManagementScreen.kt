package com.example.orphanapp.ui

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

data class StaffMember(val name: String, val role: String)
data class Task(val description: String, val status: String)

@Composable
fun StaffManagementScreen(navController: NavController) {
    val staffMembers = listOf(
        StaffMember("Alice", "Administrator"),
        StaffMember("Bob", "Caregiver"),
        StaffMember("Charlie", "Cook")
    )

    val tasks = listOf(
        Task("Prepare lunch", "In Progress"),
        Task("Organize outdoor activities", "Pending"),
        Task("Submit weekly report", "Completed")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Staff Management", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Staff Members", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(staffMembers) { staff ->
                        Text("${staff.name} - ${staff.role}", modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Task Assignment & Tracking", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(tasks) { task ->
                        Text("${task.description} - ${task.status}", modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StaffManagementScreenPreview() {
    OrphanAppTheme {
        StaffManagementScreen(rememberNavController())
    }
}
