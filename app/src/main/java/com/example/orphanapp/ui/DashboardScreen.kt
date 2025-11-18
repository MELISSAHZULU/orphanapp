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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.orphanapp.R
import com.example.orphanapp.data.Orphan // Updated import
import com.example.orphanapp.viewmodel.AuthState
import com.example.orphanapp.viewmodel.AuthViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    orphans: List<Orphan>
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val totalOrphans = orphans.size
    val activeOrphans = orphans.count { it.status == "Active" }
    val pendingVerification = totalOrphans - activeOrphans
    val totalBeds = 100 // Example value
    val availableBeds = totalBeds - activeOrphans
    val recentAdmission = orphans.lastOrNull()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                navController = navController,
                authViewModel = authViewModel,
                closeDrawer = { scope.launch { drawerState.close() } }
            )
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
                    actions = {
                        IconButton(onClick = { navController.navigate("announcements") }) {
                            Icon(Icons.Filled.Campaign, contentDescription = "Announcements")
                        }
                        IconButton(onClick = { navController.navigate("user_profile") }) {
                            Icon(Icons.Filled.AccountCircle, contentDescription = "User Profile")
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
            Box(modifier = Modifier.fillMaxSize()) { // Wrap content in a Box for background
                Image(
                    painter = painterResource(id = R.drawable.dashboard_background),
                    contentDescription = "Dashboard Background",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = 0.5f // Make background slightly transparent
                )

                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        DashboardCard(icon = Icons.Filled.People, title = "Total Orphans", value = totalOrphans.toString(), onClick = { navController.navigate("total_orphans") })
                        DashboardCard(icon = Icons.Filled.CheckCircle, title = "Verified", value = activeOrphans.toString(), onClick = { navController.navigate("verified_orphans") })
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        DashboardCard(icon = Icons.Filled.HourglassTop, title = "Pending", value = pendingVerification.toString(), onClick = { navController.navigate("pending_verification") })
                        DashboardCard(icon = Icons.Filled.Bed, title = "Available Beds", value = availableBeds.toString(), onClick = { navController.navigate("available_beds") })
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        DashboardCard(icon = Icons.Filled.PhotoLibrary, title = "Photo Gallery", value = "View", onClick = { navController.navigate("photo_gallery") })
                        DashboardCard(icon = Icons.Filled.Favorite, title = "Donations", value = "View", onClick = { navController.navigate("donation") })
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = { navController.navigate("enrollment") }, modifier = Modifier.fillMaxWidth()) {
                        Text("Enroll New Orphan")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(onClick = { navController.navigate("report") }, modifier = Modifier.fillMaxWidth()) {
                        Text("Generate Report", color = MaterialTheme.colorScheme.primary)
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text("Recent Admissions", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    if (recentAdmission != null) {
                        RecentAdmissionItem(
                            navController = navController,
                            orphan = recentAdmission
                        )
                    } else {
                        Text("No recent admissions")
                    }
                }
            }
        }
    }
}

@Composable
fun AppDrawer(
    navController: NavController,
    authViewModel: AuthViewModel,
    closeDrawer: () -> Unit
) {
    val authState by authViewModel.authState.collectAsState()
    val user = (authState as? AuthState.Authenticated)?.user

    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.primary
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate("user_profile"); closeDrawer() }
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground), // Placeholder
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = user?.displayName ?: user?.email?.substringBefore('@') ?: "User",
                    color = MaterialTheme.colorScheme.onPrimary, 
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = user?.email ?: "",
                    color = MaterialTheme.colorScheme.onPrimary, 
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            // Menu Items
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                DrawerItem(icon = Icons.Filled.Home, text = "Home", onClick = { navController.navigate("dashboard"); closeDrawer() })
                DrawerItem(icon = Icons.Filled.Inventory, text = "Inventory", onClick = { navController.navigate("inventory"); closeDrawer() })
                DrawerItem(icon = Icons.Filled.Assignment, text = "Activity Log", onClick = { navController.navigate("activity_log"); closeDrawer() })
                DrawerItem(icon = Icons.AutoMirrored.Filled.Message, text = "Communication", onClick = { navController.navigate("communication"); closeDrawer() })
                DrawerItem(icon = Icons.Filled.BarChart, text = "Impact Reporting", onClick = { navController.navigate("impact_reporting"); closeDrawer() })
                DrawerItem(icon = Icons.Filled.SupervisorAccount, text = "Staff Management", onClick = { navController.navigate("staff_management"); closeDrawer() })
                DrawerItem(icon = Icons.Filled.Settings, text = "Settings", onClick = { navController.navigate("settings"); closeDrawer() })
            }
            Spacer(modifier = Modifier.weight(1f))
            // Footer
            DrawerItem(icon = Icons.AutoMirrored.Filled.Logout, text = "Log Out", onClick = {
                authViewModel.signOut()
                closeDrawer()
            })
        }
    }
}

@Composable
fun DrawerItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = text, tint = MaterialTheme.colorScheme.onPrimary) },
        label = { Text(text, color = MaterialTheme.colorScheme.onPrimary) },
        selected = false,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = Color.Transparent)
    )
}

@Composable
fun DashboardCard(icon: ImageVector, title: String, value: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(150.dp)
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, textAlign = TextAlign.Center, fontSize = 14.sp, lineHeight = 16.sp)
            if (value.isNotEmpty()) {
                Text(text = value, fontWeight = FontWeight.Bold, fontSize = 24.sp)
            }
        }
    }
}

@Composable
fun RecentAdmissionItem(navController: NavController, orphan: Orphan) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("profile/${orphan.documentId}") },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background), // Placeholder
                contentDescription = orphan.name, 
                modifier = Modifier.size(50.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = orphan.name, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.weight(1f))
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(50))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(orphan.status, color = MaterialTheme.colorScheme.onPrimary, fontSize = 12.sp)
                    }
                }
                Text("Age ${orphan.age}", fontSize = 14.sp)
                Text(orphan.guardianName, fontSize = 12.sp)
            }
        }
    }
}
