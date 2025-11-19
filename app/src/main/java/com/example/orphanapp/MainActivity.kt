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
import com.example.orphanapp.data.Orphan
import com.example.orphanapp.ui.*
import com.example.orphanapp.ui.theme.OrphanAppTheme
import com.example.orphanapp.viewmodel.*

class MainActivity : ComponentActivity() {
    private val authViewModel: AuthViewModel by viewModels { AuthViewModelFactory((application as OrphanApplication).authRepository) }
    private val enrollmentViewModel: EnrollmentViewModel by viewModels { EnrollmentViewModelFactory((application as OrphanApplication).orphanRepository) }
    private val donationViewModel: DonationViewModel by viewModels { DonationViewModelFactory((application as OrphanApplication).donationRepository) }
    private val staffViewModel: StaffViewModel by viewModels { StaffViewModelFactory((application as OrphanApplication).staffRepository) }
    private val inventoryViewModel: InventoryViewModel by viewModels { InventoryViewModelFactory((application as OrphanApplication).inventoryRepository) }
    private val activityLogViewModel: ActivityLogViewModel by viewModels { ActivityLogViewModelFactory((application as OrphanApplication).activityLogRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrphanAppTheme {
                OrphanageApp(authViewModel, enrollmentViewModel, donationViewModel, staffViewModel, inventoryViewModel, activityLogViewModel)
            }
        }
    }
}

@Composable
fun OrphanageApp(
    authViewModel: AuthViewModel,
    enrollmentViewModel: EnrollmentViewModel,
    donationViewModel: DonationViewModel,
    staffViewModel: StaffViewModel,
    inventoryViewModel: InventoryViewModel,
    activityLogViewModel: ActivityLogViewModel
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
                    EnrollmentScreen(navController, enrollmentViewModel)
                }
                composable("tracking") {
                    TrackingScreen(orphanList, navController)
                }
                composable("profile/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")
                    val orphan = orphanList.find { it.documentId == id }
                    OrphanProfileScreen(navController, orphan) { updatedOrphan ->
                        enrollmentViewModel.updateOrphan(updatedOrphan)
                    }
                }
                 composable("pending_profile/{id}") { backStackEntry ->
                    val id = backStackEntry.arguments?.getString("id")
                    val orphan = orphanList.find { it.documentId == id }
                    PendingProfileScreen(navController, orphan, 
                        onAccept = { orphanId ->
                            enrollmentViewModel.acceptOrphan(orphanId)
                            navController.popBackStack()
                        },
                        onDecline = { orphanId ->
                            enrollmentViewModel.declineOrphan(orphanId)
                            navController.popBackStack()
                        }
                    )
                }
                composable("record_detail/{title}/{content}") { backStackEntry ->
                    val title = backStackEntry.arguments?.getString("title")
                    val content = backStackEntry.arguments?.getString("content")
                    RecordDetailScreen(navController, title, content)
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
                composable("add_photo") {
                    AddPhotoScreen(navController)
                }
                composable("activity_log") {
                    ActivityLogScreen(navController, activityLogViewModel)
                }
                composable("donation") {
                    DonationScreen(navController, donationViewModel)
                }
                composable("donation_history") {
                    DonationHistoryScreen(navController, donationViewModel)
                }
                composable("impact_reporting") {
                    ImpactReportingScreen(navController)
                }
                composable("inventory") {
                    InventoryScreen(navController, inventoryViewModel)
                }
                composable("staff_management") {
                    StaffManagementScreen(navController, staffViewModel)
                }
                composable("add_edit_staff/{staffId}") { backStackEntry ->
                    val staffId = backStackEntry.arguments?.getString("staffId")
                    AddEditStaffScreen(navController, staffViewModel, staffId)
                }
                composable("add_edit_staff") {
                    AddEditStaffScreen(navController, staffViewModel, null)
                }
                composable("communication") {
                    CommunicationScreen(navController)
                }
                composable("conversation/{contactName}") { backStackEntry ->
                    val contactName = backStackEntry.arguments?.getString("contactName")
                    ConversationScreen(navController, contactName)
                }
                composable("announcements") {
                    AnnouncementsScreen(navController)
                }
                composable("privacy_policy") {
                    PrivacyPolicyScreen(navController)
                }
                composable("user_profile") {
                    UserProfileScreen(navController, authViewModel)
                }
                composable("edit_profile") {
                    EditProfileScreen(navController, authViewModel)
                }
                composable("change_password") {
                    ChangePasswordScreen(navController)
                }
                composable("add_edit_inventory_item/{itemId}") { backStackEntry ->
                    val itemId = backStackEntry.arguments?.getString("itemId")
                    AddEditInventoryItemScreen(navController, inventoryViewModel, itemId)
                }
                composable("add_edit_inventory_item") {
                    AddEditInventoryItemScreen(navController, inventoryViewModel, null)
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
