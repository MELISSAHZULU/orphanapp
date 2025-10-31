package com.example.orphanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.orphanapp.model.Orphan
import com.example.orphanapp.ui.ChecklistScreen
import com.example.orphanapp.ui.DashboardScreen
import com.example.orphanapp.ui.EnrollmentScreen
import com.example.orphanapp.ui.TrackingScreen
import com.example.orphanapp.ui.OrphanProfileScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrphanageApp()
        }
    }
}

@Composable
fun OrphanageApp() {
    val navController = rememberNavController()
    val orphanList = remember { mutableStateListOf<Orphan>() }

    NavHost(navController = navController, startDestination = "dashboard") {
        composable("dashboard") {
            DashboardScreen(navController)
        }
        composable("enrollment") {
            EnrollmentScreen(navController, orphanList) { newOrphanId ->
                navController.navigate("profile/$newOrphanId")
            }
        }
        composable("tracking") {
            TrackingScreen(orphanList, navController)
        }
        composable("profile/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            val orphan = orphanList.find { it.id == id }
            OrphanProfileScreen(navController, orphan)
        }
        composable("checklist") {
            ChecklistScreen(navController)
        }
    }
}
