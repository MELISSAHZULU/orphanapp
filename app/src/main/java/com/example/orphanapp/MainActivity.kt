package com.example.orphanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.example.orphanapp.model.Orphan
import com.example.orphanapp.ui.AboutScreen
import com.example.orphanapp.ui.AvailableBedsScreen
import com.example.orphanapp.ui.BottomNavigationBar
import com.example.orphanapp.ui.ChecklistScreen
import com.example.orphanapp.ui.DashboardScreen
import com.example.orphanapp.ui.EnrollmentScreen
import com.example.orphanapp.ui.HelpScreen
import com.example.orphanapp.ui.LoginScreen
import com.example.orphanapp.ui.OrphanProfileScreen
import com.example.orphanapp.ui.PendingVerificationScreen
import com.example.orphanapp.ui.SettingsScreen
import com.example.orphanapp.ui.TotalOrphansScreen
import com.example.orphanapp.ui.TrackingScreen
import com.example.orphanapp.ui.VerifiedOrphansScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkMode = remember { mutableStateOf(false) }
            MaterialTheme(
                colorScheme = if (isDarkMode.value) darkColorScheme() else lightColorScheme()
            ) {
                OrphanageApp(isDarkMode)
            }
        }
    }
}

@Composable
fun OrphanageApp(isDarkMode: MutableState<Boolean>) {
    val navController = rememberNavController()
    val orphanList = remember { mutableStateListOf<Orphan>() }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
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
        composable("settings") {
            SettingsScreen(navController, isDarkMode)
        }
        composable("about") {
            AboutScreen(navController)
        }
        composable("help") {
            HelpScreen(navController)
        }
        composable("total_orphans") {
            TotalOrphansScreen(navController, orphanList)
        }
        composable("verified_orphans") {
            VerifiedOrphansScreen(navController, orphanList.filter { it.status == "Active" })
        }
        composable("pending_verification") {
            PendingVerificationScreen(navController, orphanList.filter { it.status != "Active" })
        }
        composable("available_beds") {
            AvailableBedsScreen(navController)
        }
    }
}
