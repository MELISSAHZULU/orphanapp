package com.example.orphanapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.orphanapp.data.Orphan
import com.example.orphanapp.viewmodel.EnrollmentViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrphanProfileScreen(
    navController: NavController, 
    orphanId: String,
    viewModel: EnrollmentViewModel, 
    onUpdate: (Orphan) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }

    // Fetch the specific orphan's details
    LaunchedEffect(orphanId) {
        viewModel.getOrphanById(orphanId)
    }
    val orphan by viewModel.selectedOrphan.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (orphan != null) "Profile: ${orphan?.name}" else "Loading...") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                actions = {
                    if (!isEditing && orphan != null) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                if (orphan == null) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    if (isEditing) {
                        EditProfileView(orphan = orphan!!, onUpdate = onUpdate, onCancel = { isEditing = false })
                    } else {
                        ViewProfileView(orphan = orphan!!, navController = navController)
                    }
                }
            }
        }
    )
}

@Composable
fun EditProfileView(orphan: Orphan, onUpdate: (Orphan) -> Unit, onCancel: () -> Unit) {
    var name by remember { mutableStateOf(orphan.name) }
    var age by remember { mutableStateOf(orphan.age.toString()) }
    var gender by remember { mutableStateOf(orphan.gender) }
    var healthRecords by remember { mutableStateOf(orphan.healthRecords) }
    var orphanStory by remember { mutableStateOf(orphan.orphanStory) }
    var sponsorInfo by remember { mutableStateOf(orphan.sponsorInfo) }
    var educationProgress by remember { mutableStateOf(orphan.educationProgress) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        Text("Records", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = healthRecords, onValueChange = { healthRecords = it }, label = { Text("Health Records") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = orphanStory, onValueChange = { orphanStory = it }, label = { Text("Orphan Story") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = sponsorInfo, onValueChange = { sponsorInfo = it }, label = { Text("Sponsor Info") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = educationProgress, onValueChange = { educationProgress = it }, label = { Text("Education Progress") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = onCancel) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                val updatedOrphan = orphan.copy(
                    name = name,
                    age = age.toIntOrNull() ?: orphan.age,
                    gender = gender,
                    healthRecords = healthRecords,
                    orphanStory = orphanStory,
                    sponsorInfo = sponsorInfo,
                    educationProgress = educationProgress
                )
                onUpdate(updatedOrphan)
                onCancel() // To exit edit mode
            }) {
                Text("Save")
            }
        }
    }
}


@Composable
fun ViewProfileView(orphan: Orphan, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = orphan.photoUrl,
                contentDescription = "Orphan Photo",
                modifier = Modifier.size(150.dp).clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_launcher_background),
                error = painterResource(R.drawable.ic_launcher_background)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(orphan.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }

        InfoRow("Age", orphan.age.toString())
        InfoRow("Gender", orphan.gender)
        InfoRow("Enrollment Date", orphan.enrollmentDate)
        InfoRow("Status", orphan.status)

        Spacer(modifier = Modifier.height(24.dp))

        Text("Records", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))

        RecordLink(navController, "Health Records", orphan.healthRecords)
        RecordLink(navController, "Orphan Story", orphan.orphanStory)
        RecordLink(navController, "Sponsor Info", orphan.sponsorInfo)
        RecordLink(navController, "Education Progress", orphan.educationProgress)

        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Back to List")
        }
    }
}

@Composable
fun RecordLink(navController: NavController, title: String, content: String) {
    val encodedContent = URLEncoder.encode(content, StandardCharsets.UTF_8.toString())
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                if (content.isNotBlank()) {
                    navController.navigate("record_detail/$title/$encodedContent")
                }
            },
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(if (content.isNotBlank()) "View" else "N/A", color = MaterialTheme.colorScheme.primary)
        }
    }
}
