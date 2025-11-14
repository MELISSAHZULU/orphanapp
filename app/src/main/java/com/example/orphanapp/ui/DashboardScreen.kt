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
import androidx.compose.material3.NavigationDrawerItemDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.orphanapp.R
import com.example.orphanapp.model.Orphan
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
    val pendingVerification = orphans.count { it.status != "Active" }
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
                        IconButton(onClick = { /* Navigate to user profile */ }) {
                            Icon(Icons.Filled.AccountCircle, contentDescription = "User Profile", modifier = Modifier.size(40.dp))
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
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    DashboardCard(icon = Icons.Filled.People, title = "Total Orphans Registered", value = totalOrphans.toString(), onClick = { navController.navigate("total_orphans") })
                    DashboardCard(icon = Icons.Filled.CheckCircle, title = "Verified & Admitted", value = (totalOrphans - pendingVerification).toString(), onClick = { navController.navigate("verified_orphans") })
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    DashboardCard(icon = Icons.Filled.HourglassTop, title = "Pending Verification", value = pendingVerification.toString(), onClick = { navController.navigate("pending_verification") })
                    DashboardCard(icon = Icons.Filled.Bed, title = "Available Beds", value = "20", onClick = { navController.navigate("available_beds") })
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    DashboardCard(icon = Icons.Filled.PhotoLibrary, title = "Photo Gallery", value = "", onClick = { navController.navigate("photo_gallery") })
                    DashboardCard(icon = Icons.Filled.Favorite, title = "Donations", value = "", onClick = { navController.navigate("donation") })
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
                if (recentAdmission != null) {
                    RecentAdmissionItem(
                        navController = navController,
                        id = recentAdmission.id,
                        name = recentAdmission.name,
                        age = recentAdmission.age,
                        home = recentAdmission.guardianName,
                        imageUrl = R.drawable.ic_launcher_background // Replace with actual image if available
                    )
                } else {
                    Text("No recent admissions")
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
    val scrollState = rememberScrollState()
    val authState by authViewModel.authState.collectAsState()
    val user = (authState as? AuthState.Authenticated)?.user
    val drawerColor = MaterialTheme.colorScheme.primary
    val textColor = MaterialTheme.colorScheme.onPrimary

    ModalDrawerSheet(
        drawerContainerColor = drawerColor
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
                Spacer(modifier = Modifier.height(8.dp))
                val displayName = user?.email?.substringBefore('@')?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } ?: "Alexandra"
                Text(text = displayName, color = textColor, style = MaterialTheme.typography.headlineSmall)
                Text(text = user?.email ?: "alexandra@gmail.com", color = textColor, style = MaterialTheme.typography.bodyMedium)
            }
            // Menu Items
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                DrawerItem(icon = Icons.Filled.Home, text = "Home", onClick = { navController.navigate("dashboard"); closeDrawer() })
                DrawerItem(icon = Icons.Filled.Inventory, text = "Inventory", onClick = { navController.navigate("inventory"); closeDrawer() })
                DrawerItem(icon = Icons.Filled.Assessment, text = "Reports", onClick = { navController.navigate("report"); closeDrawer() })
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
    val textColor = MaterialTheme.colorScheme.onPrimary
    NavigationDrawerItem(
        icon = { Icon(icon, contentDescription = text, tint = textColor) },
        label = { Text(text, color = textColor) },
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
