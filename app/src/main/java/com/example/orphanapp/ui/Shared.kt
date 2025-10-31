package com.example.orphanapp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

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
