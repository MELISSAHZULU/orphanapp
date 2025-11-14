package com.example.orphanapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.orphanapp.model.Orphan
import com.example.orphanapp.viewmodel.EnrollmentViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnrollmentScreen(
    navController: NavController,
    viewModel: EnrollmentViewModel,
    onEnrollmentSuccess: (Int) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var guardianName by remember { mutableStateOf("") }
    var schoolName by remember { mutableStateOf("") }
    var ageInRange by remember { mutableStateOf(false) }
    var storySummaryProvided by remember { mutableStateOf(false) }
    var healthInfoProvided by remember { mutableStateOf(false) }
    var residentialProgram by remember { mutableStateOf(false) }
    var orphanStatusProvided by remember { mutableStateOf(false) }
    var photoInserted by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Enrollment") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Orphan Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Select Gender", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.fillMaxWidth())
                        Row {
                            ChecklistItem(label = "Male", checked = gender == "Male", onCheckedChange = { gender = "Male" })
                            ChecklistItem(label = "Female", checked = gender == "Female", onCheckedChange = { gender = "Female" })
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(value = guardianName, onValueChange = { guardianName = it }, label = { Text("Guardian Name") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp))
                    }
                }
            }
            item {
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Checklist", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        ChecklistItem(label = "Age between 1-18", checked = ageInRange, onCheckedChange = { ageInRange = it })
                        ChecklistItem(label = "Summary of story provided", checked = storySummaryProvided, onCheckedChange = { storySummaryProvided = it })
                        ChecklistItem(label = "Health information provided", checked = healthInfoProvided, onCheckedChange = { healthInfoProvided = it })
                        ChecklistItem(label = "Residential Program Provided", checked = residentialProgram, onCheckedChange = { residentialProgram = it })
                        ChecklistItem(label = "Orphan status provided", checked = orphanStatusProvided, onCheckedChange = { orphanStatusProvided = it })
                        ChecklistItem(label = "Photo Inserted", checked = photoInserted, onCheckedChange = { photoInserted = it })
                    }
                }
            }
            item {
                Button(
                    onClick = {
                        val missingFields = mutableListOf<String>()
                        if (name.isBlank()) missingFields.add("Name")
                        if (age.isBlank()) missingFields.add("Age")
                        if (gender.isBlank()) missingFields.add("Gender")
                        if (guardianName.isBlank()) missingFields.add("Guardian Name")
                        if (!ageInRange) missingFields.add("Age between 1-18")
                        if (!storySummaryProvided) missingFields.add("Summary of story")
                        if (!healthInfoProvided) missingFields.add("Health information")
                        if (!residentialProgram) missingFields.add("Residential Program")
                        if (!orphanStatusProvided) missingFields.add("Orphan status")
                        if (!photoInserted) missingFields.add("Photo")

                        if (missingFields.isNotEmpty()) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Enrollment Unsuccessful: Please provide ${missingFields.joinToString()}")
                            }
                        } else {
                            val newOrphan = Orphan(
                                id = (viewModel.orphans.value.size + 1),
                                name = name,
                                age = age.toInt(),
                                gender = gender,
                                guardianName = guardianName,
                                schoolName = schoolName,
                                status = "Pending"
                            )
                            viewModel.addOrphan(newOrphan)
                            navController.navigate("profile/${newOrphan.id}")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enroll")
                }
            }
        }
    }
}

@Composable
fun ChecklistItem(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(label)
    }
}
