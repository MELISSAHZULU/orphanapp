package com.example.orphanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.orphanapp.model.Orphan
import com.example.orphanapp.ui.AboutScreen
import com.example.orphanapp.ui.ActivityLogScreen
import com.example.orphanapp.ui.AvailableBedsScreen
import com.example.orphanapp.ui.BottomNavigationBar
import com.example.orphanapp.ui.ChecklistScreen
import com.example.orphanapp.ui.CommunicationScreen
import com.example.orphanapp.ui.DashboardScreen
import com.example.orphanapp.ui.DonationScreen
import com.example.orphanapp.ui.EnrollmentScreen
import com.example.orphanapp.ui.HelpScreen
import com.example.orphanapp.ui.ImpactReportingScreen
import com.example.orphanapp.ui.InventoryScreen
import com.example.orphanapp.ui.LoginScreen
import com.example.orphanapp.ui.OrphanProfileScreen
import com.example.orphanapp.ui.PendingVerificationScreen
import com.example.orphanapp.ui.PhotoGalleryScreen
import com.example.orphanapp.ui.ReportScreen
import com.example.orphanapp.ui.SettingsScreen
import com.example.orphanapp.ui.StaffManagementScreen
import com.example.orphanapp.ui.TotalOrphansScreen
import com.example.orphanapp.ui.TrackingScreen
import com.example.orphanapp.ui.VerifiedOrphansScreen
import com.example.orphanapp.ui.theme.OrphanAppTheme
import com.example.orphanapp.viewmodel.EnrollmentViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkMode = remember { mutableStateOf(false) }
            OrphanAppTheme(darkTheme = isDarkMode.value) {
                OrphanageApp(isDarkMode)
            }
        }
    }
}

@Composable
fun OrphanageApp(isDarkMode: MutableState<Boolean>) {
    val navController = rememberNavController()
    val enrollmentViewModel: EnrollmentViewModel = viewModel()
    val orphanList by enrollmentViewModel.orphans.collectAsState()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("dashboard") {
            DashboardScreen(navController)
        }
        composable("enrollment") {
            EnrollmentScreen(navController, enrollmentViewModel) { newOrphanId ->
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
        composable("report") {
            ReportScreen(navController, orphanList)
        }
        composable("photo_gallery") {
            PhotoGalleryScreen(navController)
        }
        composable("activity_log") {
            ActivityLogScreen(navController)
        }
        composable("donation") {
            DonationScreen(navController)
        }
        composable("impact_reporting") {
            ImpactReportingScreen(navController)
        }
        composable("inventory") {
            InventoryScreen(navController)
        }
        composable("staff_management") {
            StaffManagementScreen(navController)
        }
        composable("communication") {
            CommunicationScreen(navController)
        }
    }
}
