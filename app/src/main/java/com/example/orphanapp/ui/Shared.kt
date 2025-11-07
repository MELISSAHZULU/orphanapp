package com.example.orphanapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun AppLogo() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Adult",
                    modifier = Modifier.size(40.dp),
                    tint = Color(0xFF2196F3)
                )
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Heart",
                    modifier = Modifier.size(24.dp),
                    tint = Color(0xFFFFCC33)
                )
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = "Child",
                    modifier = Modifier.size(40.dp),
                    tint = Color(0xFF4CAF50)
                )
            }
        }
        Spacer(modifier = Modifier.padding(4.dp))
        Text("YOU & I", fontWeight = FontWeight.SemiBold, fontSize = 22.sp, color = Color(0xFF333333))
        Text("Orphan Care Network", fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String?) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Dashboard, contentDescription = "Dashboard") },
            label = { Text("Dashboard") },
            selected = currentRoute == "dashboard",
            onClick = { navController.navigate("dashboard") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Checklist, contentDescription = "Checklist") },
            label = { Text("Checklist") },
            selected = currentRoute == "checklist",
            onClick = { navController.navigate("checklist") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.PersonAdd, contentDescription = "Enrollment") },
            label = { Text("Enrollment") },
            selected = currentRoute == "enrollment",
            onClick = { navController.navigate("enrollment") }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = currentRoute == "profile",
            onClick = { navController.navigate("profile/1") } // Default to orphan 1
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.People, contentDescription = "Tracking") },
            label = { Text("Tracking") },
            selected = currentRoute == "tracking",
            onClick = { navController.navigate("tracking") }
        )
    }
}
