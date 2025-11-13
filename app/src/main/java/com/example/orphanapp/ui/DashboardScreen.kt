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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bed
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.orphanapp.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(modifier = Modifier.fillMaxSize()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    NavigationDrawerItem(
                        label = { Text("Activity Log") },
                        selected = false,
                        onClick = { navController.navigate("activity_log") }
                    )
                    NavigationDrawerItem(
                        label = { Text("Donations") },
                        selected = false,
                        onClick = { navController.navigate("donation") }
                    )
                    NavigationDrawerItem(
                        label = { Text("Impact Reporting") },
                        selected = false,
                        onClick = { navController.navigate("impact_reporting") }
                    )
                    NavigationDrawerItem(
                        label = { Text("Inventory") },
                        selected = false,
                        onClick = { navController.navigate("inventory") }
                    )
                    NavigationDrawerItem(
                        label = { Text("Staff Management") },
                        selected = false,
                        onClick = { navController.navigate("staff_management") }
                    )
                    NavigationDrawerItem(
                        label = { Text("Communication") },
                        selected = false,
                        onClick = { navController.navigate("communication") }
                    )
                    NavigationDrawerItem(
                        label = { Text("Settings") },
                        selected = false,
                        onClick = { navController.navigate("settings") }
                    )
                    NavigationDrawerItem(
                        label = { Text("About") },
                        selected = false,
                        onClick = { navController.navigate("about") }
                    )
                    NavigationDrawerItem(
                        label = { Text("Help & Support") },
                        selected = false,
                        onClick = { navController.navigate("help") }
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    NavigationDrawerItem(
                        label = { Text("Logout") },
                        selected = false,
                        onClick = { navController.navigate("login") },
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("YOU AND I", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Menu", modifier = Modifier.size(40.dp))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            bottomBar = {
                BottomNavigationBar(navController, "dashboard")
            }
        ) { padding ->
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(scrollState)
            ) {
                Text("Dashboard", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    DashboardCard(icon = Icons.Filled.People, title = "Total Orphans Registered", value = "2,356", onClick = { navController.navigate("total_orphans") })
                    DashboardCard(icon = Icons.Filled.CheckCircle, title = "Verified & Admitted", value = "1,980", onClick = { navController.navigate("verified_orphans") })
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    DashboardCard(icon = Icons.Filled.HourglassTop, title = "Pending Verification", value = "150", onClick = { navController.navigate("pending_verification") })
                    DashboardCard(icon = Icons.Filled.Bed, title = "Available Beds", value = "20", onClick = { navController.navigate("available_beds") })
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    DashboardCard(icon = Icons.Filled.PhotoLibrary, title = "Photo Gallery", value = "", onClick = { navController.navigate("photo_gallery") })
                    DashboardCard(icon = Icons.Filled.Assignment, title = "Activity Log", value = "", onClick = { navController.navigate("activity_log") })
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    DashboardCard(icon = Icons.Filled.Favorite, title = "Donations", value = "", onClick = { navController.navigate("donation") })
                    DashboardCard(icon = Icons.Filled.BarChart, title = "Impact Reporting", value = "", onClick = { navController.navigate("impact_reporting") })
                }
                 Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    DashboardCard(icon = Icons.Filled.Inventory, title = "Inventory", value = "", onClick = { navController.navigate("inventory") })
                    DashboardCard(icon = Icons.Filled.SupervisorAccount, title = "Staff Management", value = "", onClick = { navController.navigate("staff_management") })
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    DashboardCard(icon = Icons.Filled.Message, title = "Communication", value = "", onClick = { navController.navigate("communication") })
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = { navController.navigate("checklist") }, modifier = Modifier.fillMaxWidth()) {
                    Text("Register New Orphan")
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = { navController.navigate("report") }, modifier = Modifier.fillMaxWidth()) {
                    Text("Generate Report", color = MaterialTheme.colorScheme.primary)
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text("Recent Admissions", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                RecentAdmissionItem(navController = navController, id = 1, name = "Elena Khumbo", age = 5, home = "Melissahh Zulu Tsalima wa's Home", imageUrl = R.drawable.ic_launcher_background)
            }
        }
    }
}

@Composable
fun DashboardCard(icon: ImageVector, title: String, value: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(150.dp)
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
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
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Admitted", color = MaterialTheme.colorScheme.onPrimary, fontSize = 12.sp)
                    }
                }
                Text("Age $age", fontSize = 14.sp)
                Text(home, fontSize = 12.sp)
            }
        }
    }
}
