package com.example.orphanapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.orphanapp.R
import com.example.orphanapp.viewmodel.AuthState
import com.example.orphanapp.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(navController: NavController, authViewModel: AuthViewModel) {
    val authState by authViewModel.authState.collectAsState()
    val user = (authState as? AuthState.Authenticated)?.user

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Profile") },
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // Placeholder
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
                IconButton(
                    onClick = { /* TODO: Handle picture upload */ },
                    modifier = Modifier.align(Alignment.BottomEnd)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Change Picture", tint = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                InfoRow(label = "Display Name", value = user?.displayName ?: "N/A")
                InfoRow(label = "Email Address", value = user?.email ?: "N/A")
                // In a real app, role would come from a custom claim or Firestore
                InfoRow(label = "User Role", value = "Health Worker") 
                
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { navController.navigate("edit_profile") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Edit Profile")
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { navController.navigate("change_password") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Change Password")
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        Divider(modifier = Modifier.padding(top = 8.dp))
    }
}
