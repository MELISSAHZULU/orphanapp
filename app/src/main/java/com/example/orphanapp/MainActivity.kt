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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.orphanapp.data.Orphan
import com.example.orphanapp.repository.ConversationRepository
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
    private val userListViewModel: UserListViewModel by viewModels { UserListViewModelFactory((application as OrphanApplication).authRepository) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrphanAppTheme {
                val conversationRepository = (application as OrphanApplication).conversationRepository
                OrphanageApp(authViewModel, enrollmentViewModel, donationViewModel, staffViewModel, inventoryViewModel, activityLogViewModel, userListViewModel, conversationRepository)
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
    activityLogViewModel: ActivityLogViewModel,
    userListViewModel: UserListViewModel,
    conversationRepository: ConversationRepository
) {
    val navController = rememberNavController()
    val orphanList by enrollmentViewModel.orphans.collectAsState()
    val authState by authViewModel.authState.collectAsState()

    when (val state = authState) {
        is AuthState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is AuthState.Authenticated -> {
            // When the user is authenticated, check for their organizationId and load the orphans.
            LaunchedEffect(state.user.organizationId) {
                state.user.organizationId?.let { orgId ->
                    if (orgId.isNotBlank()) {
                        enrollmentViewModel.loadOrphansForOrganization(orgId)
                    }
                }
            }

            val currentUserId = state.user.uid
            NavHost(navController = navController, startDestination = "dashboard") {
                composable("dashboard") {
                    DashboardScreen(navController, authViewModel, orphanList)
                }
                composable("enrollment") {
                    EnrollmentScreen(navController, enrollmentViewModel, authViewModel)
                }
                composable("tracking") {
                    TrackingScreen(orphanList, navController)
                }
                composable("profile/{id}") { backStackEntry ->
                    backStackEntry.arguments?.getString("id")?.let { id ->
                        OrphanProfileScreen(
                            navController = navController,
                            orphanId = id,
                            viewModel = enrollmentViewModel, // Pass the ViewModel
                            onUpdate = { updatedOrphan ->
                                enrollmentViewModel.updateOrphan(updatedOrphan)
                            }
                        )
                    }
                }
                 composable("pending_profile/{id}") { backStackEntry ->
                    backStackEntry.arguments?.getString("id")?.let { id ->
                        PendingProfileScreen(
                            navController = navController,
                            orphanId = id,
                            viewModel = enrollmentViewModel, // Pass the ViewModel
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
                }
                composable("record_detail/{title}/{content}") { backStackEntry ->
                    val title = backStackEntry.arguments?.getString("title")
                    val content = backStackEntry.arguments?.getString("content")
                    RecordDetailScreen(navController, title, content)
                }
                composable("communication") {
                    CommunicationScreen(navController)
                }
                composable("new_conversation") {
                    NewConversationScreen(navController, userListViewModel, currentUserId)
                }
                composable("conversation/{conversationId}") { backStackEntry ->
                    val conversationId = backStackEntry.arguments?.getString("conversationId")
                    if (conversationId != null) {
                        val conversationViewModel: ConversationViewModel = viewModel(
                            factory = ConversationViewModelFactory(conversationRepository, currentUserId)
                        )
                        ConversationScreen(navController, conversationViewModel, conversationId)
                    }
                }
                // ... other composables
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
