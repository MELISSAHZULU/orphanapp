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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.orphanapp.data.Orphan // Corrected Import

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrphanProfileScreen(navController: NavController, orphan: Orphan?, onUpdate: (Orphan) -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    var name by remember(orphan) { mutableStateOf(orphan?.name ?: "") }
    var age by remember(orphan) { mutableStateOf(orphan?.age?.toString() ?: "") }
    var gender by remember(orphan) { mutableStateOf(orphan?.gender ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (orphan != null) "Profile: ${orphan.name}" else "Loading...") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } },
                actions = {
                    if (orphan != null && orphan.status == "Active") {
                        IconButton(onClick = { isEditing = !isEditing }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        if (orphan == null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Loading orphan data...")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                if (isEditing) {
                    // Editing Mode UI
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender") }, modifier = Modifier.fillMaxWidth())

                } else {
                    // Read-Only UI
                    Text("Name: ${orphan.name}", style = MaterialTheme.typography.bodyLarge)
                    Text("Age: ${orphan.age}", style = MaterialTheme.typography.bodyLarge)
                    Text("Gender: ${orphan.gender}", style = MaterialTheme.typography.bodyLarge)
                    Text("Status: ${orphan.status}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.weight(1f))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (orphan.status == "Pending" && !isEditing) Arrangement.SpaceBetween else Arrangement.End
                ) {
                    if (isEditing) {
                        Button(onClick = {
                            val updatedOrphan = orphan.copy(
                                name = name,
                                age = age.toIntOrNull() ?: orphan.age,
                                gender = gender
                            )
                            onUpdate(updatedOrphan)
                            isEditing = false
                        }) {
                            Text("Save")
                        }
                    } else {
                        if (orphan.status == "Pending") {
                            Button(onClick = {
                                val updatedOrphan = orphan.copy(status = "Active")
                                onUpdate(updatedOrphan)
                                navController.popBackStack()
                            }) {
                                Text("Verify")
                            }
                            OutlinedButton(onClick = { 
                                val updatedOrphan = orphan.copy(status = "Declined")
                                onUpdate(updatedOrphan)
                                navController.popBackStack()
                            }) {
                                Text("Decline", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}