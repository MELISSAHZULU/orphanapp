package com.example.orphanapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditStaffScreen(navController: NavController, staffId: Int?) {
    var staffName by remember { mutableStateOf("") }
    var staffRole by remember { mutableStateOf("") }
    // In a real app, you would fetch the staff member's details if staffId is not null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (staffId == null) "Add Staff" else "Edit Staff") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, contentDescription = "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = staffName,
                onValueChange = { staffName = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Staff Name") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = staffRole,
                onValueChange = { staffRole = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Role") },
                singleLine = true
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { /* TODO: Save logic */ navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}
