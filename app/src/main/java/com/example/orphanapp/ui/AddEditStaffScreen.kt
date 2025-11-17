package com.example.orphanapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.orphanapp.data.AppRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditStaffScreen(navController: NavController, staffId: Int?) {
    val isEditing = staffId != null
    val staffMember = if (isEditing) AppRepository.getStaffMember(staffId!!) else null

    var staffName by remember { mutableStateOf(staffMember?.name ?: "") }
    var staffRole by remember { mutableStateOf(staffMember?.role ?: "") }
    var isActive by remember { mutableStateOf(staffMember?.isActive ?: true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Edit Staff Member" else "Add Staff Member") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } },
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
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Status:")
                Spacer(modifier = Modifier.weight(1f))
                Text(if (isActive) "Active" else "Inactive")
                Switch(
                    checked = isActive,
                    onCheckedChange = { isActive = it }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { 
                    if (isEditing) {
                        AppRepository.updateStaffMember(staffId!!, staffName, staffRole, isActive)
                    } else {
                        AppRepository.addStaff(staffName, staffRole)
                    }
                    navController.popBackStack() 
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}
