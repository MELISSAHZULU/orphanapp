package com.example.orphanapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.orphanapp.ui.*
import com.example.orphanapp.ui.theme.OrphanAppTheme
import com.example.orphanapp.viewmodel.AuthState
import com.example.orphanapp.viewmodel.AuthViewModel
import com.example.orphanapp.viewmodel.AuthViewModelFactory
import com.example.orphanapp.viewmodel.EnrollmentViewModel
import com.example.orphanapp.viewmodel.EnrollmentViewModelFactory

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels { AuthViewModelFactory((application as OrphanApplication).authRepository) }
    private val enrollmentViewModel: EnrollmentViewModel by viewModels { EnrollmentViewModelFactory((application as OrphanApplication).orphanRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrphanAppTheme {
                OrphanageApp(authViewModel, enrollmentViewModel)
            }
        }
    }
}

@Composable
fun OrphanageApp(
    authViewModel: AuthViewModel,
    enrollmentViewModel: EnrollmentViewModel
) {
    val navController = rememberNavController()
    val orphanList by enrollmentViewModel.orphans.collectAsState()
    val authState by authViewModel.authState.collectAsState()

    when (authState) {
        is AuthState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is AuthState.Authenticated -> {
            NavHost(navController = navController, startDestination = "dashboard") {
                composable("dashboard") {
                    DashboardScreen(navController, authViewModel, orphanList)
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
                    OrphanProfileScreen(navController, orphan) { updatedOrphan ->
                        enrollmentViewModel.updateOrphan(updatedOrphan)
                    }
                }
                composable("checklist") {
                    ChecklistScreen(navController)
                }
                composable("settings") {
                    SettingsScreen(navController)
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
                    AvailableBedsScreen(navController, orphanList)
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
                composable("privacy_policy") {
                    PrivacyPolicyScreen(navController)
                }
                composable("user_profile") {
                    UserProfileScreen(navController)
                }
            }
        }
        is AuthState.Unauthenticated, is AuthState.Error -> {
            NavHost(navController = navController, startDestination = "login") {
                composable("login") {
                    LoginScreen(navController, authViewModel)
                }
                composable("register") {
                    RegisterScreen(navController, authViewModel)
                }
            }
        }
    }
}
