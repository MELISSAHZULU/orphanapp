package com.example.orphanapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.orphanapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("YOU AND I", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Orphan Enrollment and Management System", fontSize = 12.sp)
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu")
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(text = { Text("Settings") }, onClick = { navController.navigate("settings") })
                            DropdownMenuItem(text = { Text("Logout") }, onClick = { navController.navigate("login") })
                            DropdownMenuItem(text = { Text("About") }, onClick = { navController.navigate("about") })
                            DropdownMenuItem(text = { Text("Help & Support") }, onClick = { navController.navigate("help") })
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF4CAF50), titleContentColor = Color.White, actionIconContentColor = Color.White)
            )
        },
        bottomBar = {
            BottomNavigationBar(navController, "dashboard")
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Dashboard", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                DashboardCard(icon = Icons.Filled.People, title = "Total Orphans Registered", value = "2,356")
                DashboardCard(icon = Icons.Filled.CheckCircle, title = "Verified & Admitted", value = "1,980")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                DashboardCard(icon = Icons.Filled.HourglassTop, title = "Pending Verification", value = "150")
                DashboardCard(icon = Icons.Filled.Bed, title = "Available Beds", value = "20")
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = { navController.navigate("checklist") }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))) {
                Text("Register New Orphan")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(onClick = { /* Handle Generate Report */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Generate Report", color = Color(0xFF4CAF50))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Recent Admissions", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            RecentAdmissionItem(navController = navController, id = 1, name = "Elena Khumbo", age = 5, home = "Melissahh Zulu Tsalima wa's Home", imageUrl = R.drawable.ic_launcher_background)
        }
    }
}

@Composable
fun DashboardCard(icon: ImageVector, title: String, value: String) {
    Card(
        modifier = Modifier
            .size(150.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = title, tint = Color(0xFF4CAF50))
            Text(text = title, textAlign = TextAlign.Center, fontSize = 14.sp)
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        }
    }
}

@Composable
fun RecentAdmissionItem(navController: NavController, id: Int, name: String, age: Int, home: String, imageUrl: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("profile/$id") },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(painter = painterResource(id = imageUrl), contentDescription = name, modifier = Modifier.size(50.dp).clip(CircleShape))
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = name, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF4CAF50), RoundedCornerShape(50))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Admitted", color = Color.White, fontSize = 12.sp)
                    }
                }
                Text("Age $age", fontSize = 14.sp)
                Text(home, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}
