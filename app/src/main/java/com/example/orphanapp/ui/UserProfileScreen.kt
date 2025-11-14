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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.orphanapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(navController: NavController) {
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
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Alexandra", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("alexandra@gmail.com", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)

            Spacer(modifier = Modifier.height(24.dp))

            // Personal Information Section
            ProfileSection(title = "Personal Information") {
                InfoRow(label = "Phone", value = "+1 234 567 890")
                InfoRow(label = "Email", value = "alexandra@gmail.com")
            }

            // Role & Permissions Section
            ProfileSection(title = "Role & Permissions") {
                InfoRow(label = "Role", value = "Admin")
                // You can add a list of permissions here
            }

            // Activity Tracking Section
            ProfileSection(title = "Activity Tracking") {
                InfoRow(label = "Last Login", value = "2024-07-28 10:00 AM")
                TextButton(onClick = { /* TODO: Download activity log */ }) {
                    Text("Download Activity Log")
                }
            }

            // Security Features Section
            ProfileSection(title = "Security") {
                TextButton(onClick = { /* TODO: Change Password */ }) {
                    Text("Change Password")
                }
                TextButton(onClick = { /* TODO: 2FA */ }) {
                    Text("Enable Two-Factor Authentication")
                }
                TextButton(onClick = { /* TODO: Logout everywhere */ }) {
                    Text("Logout from all devices")
                }
            }
        }
    }
}

@Composable
fun ProfileSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = label, fontWeight = FontWeight.Bold, modifier = Modifier.width(100.dp))
        Text(text = value)
    }
}
