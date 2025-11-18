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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.orphanapp.R
import com.example.orphanapp.data.Orphan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrphanProfileScreen(navController: NavController, orphan: Orphan?, onUpdate: (Orphan) -> Unit) {
    var isEditing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (orphan != null) "Profile: ${orphan.name}" else "Loading...") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } },
                actions = {
                    if (!isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit")
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (orphan == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            if (isEditing) {
                EditProfileView(orphan = orphan, onUpdate = onUpdate, onCancel = { isEditing = false })
            } else {
                ViewProfileView(orphan = orphan, navController = navController)
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
        // Photo and Basic Info
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = orphan.photoUrl,
                contentDescription = "Orphan Photo",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.ic_launcher_background),
                placeholder = painterResource(id = R.drawable.ic_launcher_background)
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

        // Records
        Text("Records", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        RecordDetailView(title = "Health Records", content = orphan.healthRecords)
        RecordDetailView(title = "Orphan Story", content = orphan.orphanStory)
        RecordDetailView(title = "Sponsor Info", content = orphan.sponsorInfo)
        RecordDetailView(title = "Education Progress", content = orphan.educationProgress)
        RecordDetailView(title = "Donations Received", content = orphan.donationsReceived.joinToString("\n"))
        
        Spacer(modifier = Modifier.weight(1f))

        Button(onClick = { navController.popBackStack() }, modifier = Modifier.fillMaxWidth()) {
            Text("Back to List")
        }
    }
}

@Composable
fun EditProfileView(orphan: Orphan, onUpdate: (Orphan) -> Unit, onCancel: () -> Unit) {
    var name by remember { mutableStateOf(orphan.name) }
    var age by remember { mutableStateOf(orphan.age.toString()) }
    var gender by remember { mutableStateOf(orphan.gender) }
    var status by remember { mutableStateOf(orphan.status) }
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
        // Editable Fields
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = status, onValueChange = { status = it }, label = { Text("Status") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = healthRecords, onValueChange = { healthRecords = it }, label = { Text("Health Records") }, modifier = Modifier.fillMaxWidth().height(100.dp))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = orphanStory, onValueChange = { orphanStory = it }, label = { Text("Orphan Story") }, modifier = Modifier.fillMaxWidth().height(100.dp))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = sponsorInfo, onValueChange = { sponsorInfo = it }, label = { Text("Sponsor Info") }, modifier = Modifier.fillMaxWidth().height(100.dp))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = educationProgress, onValueChange = { educationProgress = it }, label = { Text("Education Progress") }, modifier = Modifier.fillMaxWidth().height(100.dp))
        
        Spacer(modifier = Modifier.weight(1f))

        // Action Buttons
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            OutlinedButton(onClick = onCancel) {
                Text("Cancel")
            }
            Button(onClick = {
                val updatedOrphan = orphan.copy(
                    name = name,
                    age = age.toIntOrNull() ?: orphan.age,
                    gender = gender,
                    status = status,
                    healthRecords = healthRecords,
                    orphanStory = orphanStory,
                    sponsorInfo = sponsorInfo,
                    educationProgress = educationProgress
                )
                onUpdate(updatedOrphan)
                onCancel() // Go back to view mode
            }) {
                Text("Update")
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = "$label:", fontWeight = FontWeight.Bold, modifier = Modifier.width(120.dp))
        Text(text = value)
    }
}

@Composable
fun RecordDetailView(title: String, content: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Card(modifier = Modifier.fillMaxWidth().padding(top = 4.dp), elevation = CardDefaults.cardElevation(2.dp)) {
            Text(text = content.ifBlank { "No information provided." }, modifier = Modifier.padding(16.dp))
        }
    }
}
