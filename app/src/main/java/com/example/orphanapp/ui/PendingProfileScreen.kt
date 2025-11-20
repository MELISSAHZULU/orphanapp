package com.example.orphanapp.ui

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.orphanapp.R
import com.example.orphanapp.viewmodel.EnrollmentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendingProfileScreen(
    navController: NavController,
    orphanId: String,
    viewModel: EnrollmentViewModel,
    onAccept: (String) -> Unit,
    onDecline: (String) -> Unit
) {
    // Fetch the specific orphan's details
    LaunchedEffect(orphanId) {
        viewModel.getOrphanById(orphanId)
    }

    val orphan by viewModel.selectedOrphan.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (orphan != null) "Pending: ${orphan?.name}" else "Loading...") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        if (orphan == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Photo and Basic Info
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = orphan?.photoUrl,
                        contentDescription = "Orphan Photo",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.ic_launcher_background),
                        error = painterResource(R.drawable.ic_launcher_background)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(orphan?.name ?: "", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                InfoRow("Age", orphan?.age.toString())
                InfoRow("Gender", orphan?.gender ?: "N/A")
                InfoRow("Enrollment Date", orphan?.enrollmentDate ?: "N/A")
                InfoRow("Status", orphan?.status ?: "N/A")

                Spacer(modifier = Modifier.height(24.dp))

                // Records
                Text("Records", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
                RecordDetailView(title = "Health Records", content = orphan?.healthRecords ?: "N/A")
                RecordDetailView(title = "Orphan Story", content = orphan?.orphanStory ?: "N/A")
                RecordDetailView(title = "Sponsor Info", content = orphan?.sponsorInfo ?: "N/A")
                RecordDetailView(title = "Education Progress", content = orphan?.educationProgress ?: "N/A")
                RecordDetailView(title = "Donations Received", content = orphan?.donationsReceived?.joinToString("\n") ?: "N/A")

                Spacer(modifier = Modifier.weight(1f))

                // Accept and Decline Buttons
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(
                        onClick = { onAccept(orphan!!.documentId) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Accept")
                    }
                    Button(
                        onClick = { onDecline(orphan!!.documentId) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Decline")
                    }
                }
            }
        }
    }
}

@Composable
fun RecordDetailView(title: String, content: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = if (content.isNotBlank()) content else "N/A", style = MaterialTheme.typography.bodyMedium)
        }
    }
}