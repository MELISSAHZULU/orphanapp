package com.example.orphanapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.Locale

// Placeholder data class for UI development.
// In a real app, this would come from your data layer (e.g., Firestore).
data class Donor(val name: String, val amount: Double, val date: String)

// Dummy data to populate the screen for now.
val dummyDonors = listOf(
    Donor("Alice Johnson", 150.0, "2024-09-20"),
    Donor("Bob Williams", 300.0, "2024-09-18"),
    Donor("Charlie Brown", 75.50, "2024-09-15")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonorScreen(navController: NavController) {
    // In a real app, you would get this list from a ViewModel.
    val donors = dummyDonors

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Donations") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /* TODO: Navigate to an 'Add Donation' screen */ }) {
                Icon(Icons.Default.Add, contentDescription = "Add Donation")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            items(donors) { donor ->
                DonorItem(donor = donor)
            }
        }
    }
}

@Composable
fun DonorItem(donor: Donor) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = donor.name, style = MaterialTheme.typography.titleMedium)
            Text(text = String.format(Locale.getDefault(), "Amount: $%.2f", donor.amount), style = MaterialTheme.typography.bodyMedium)
            Text(text = "Date: ${donor.date}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
