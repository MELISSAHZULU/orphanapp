package com.example.orphanapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    var newOrphanNotifications by remember { mutableStateOf(true) }
    var urgentAlerts by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
                .padding(16.dp)
        ) {
            // Account Section
            SettingsGroup(title = "Account") {
                SettingsItem(text = "Edit Profile", onClick = { /* TODO */ })
                SettingsItem(text = "Change Password", onClick = { /* TODO */ })
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Notifications Section
            SettingsGroup(title = "Notifications") {
                SwitchSettingItem(
                    text = "New Orphan Registered",
                    checked = newOrphanNotifications,
                    onCheckedChange = { newOrphanNotifications = it }
                )
                SwitchSettingItem(
                    text = "Urgent Alerts",
                    checked = urgentAlerts,
                    onCheckedChange = { urgentAlerts = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // About Section
            SettingsGroup(title = "About") {
                SettingsItem(text = "Privacy Policy", onClick = { navController.navigate("privacy_policy") })
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)) {
                    Text("App Version")
                    Spacer(modifier = Modifier.weight(1f))
                    Text("1.0.0")
                }
            }
        }
    }
}

@Composable
fun SettingsGroup(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SettingsItem(text: String, onClick: () -> Unit) {
    Text(text = text, modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick)
        .padding(vertical = 16.dp))
}

@Composable
fun SwitchSettingItem(text: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
