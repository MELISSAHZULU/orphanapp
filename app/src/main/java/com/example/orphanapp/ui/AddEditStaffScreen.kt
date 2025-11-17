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
import com.example.orphanapp.data.StaffMember
import com.example.orphanapp.viewmodel.StaffViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditStaffScreen(navController: NavController, staffViewModel: StaffViewModel, staffId: String?) {
    val staffList by staffViewModel.staff.collectAsState()
    val isEditing = staffId != null
    val staffMember = if (isEditing) staffList.find { it.id == staffId } else null

    var staffName by remember { mutableStateOf(staffMember?.name ?: "") }
    var staffRole by remember { mutableStateOf(staffMember?.role ?: "") }
    var isActive by remember { mutableStateOf(staffMember?.isActive ?: true) }

    // This will update the local state if the staffMember data changes (e.g., after a remote update)
    LaunchedEffect(staffMember) {
        if (staffMember != null) {
            staffName = staffMember.name
            staffRole = staffMember.role
            isActive = staffMember.isActive
        }
    }

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
                    val updatedStaffMember = staffMember?.copy(
                        name = staffName,
                        role = staffRole,
                        isActive = isActive
                    ) ?: StaffMember(
                        id = "", // Firestore will generate this
                        name = staffName,
                        role = staffRole,
                        isActive = isActive
                    )

                    if (isEditing) {
                        staffViewModel.updateStaff(updatedStaffMember)
                    } else {
                        staffViewModel.addStaff(updatedStaffMember)
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = staffName.isNotBlank() && staffRole.isNotBlank()
            ) {
                Text("Save")
            }
        }
    }
}
