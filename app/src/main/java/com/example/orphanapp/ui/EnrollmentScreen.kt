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
    var birthCertificate by remember { mutableStateOf(false) }
    var deathCertificate by remember { mutableStateOf(false) }
    var guardianConsent by remember { mutableStateOf(false) }
    var healthRecord by remember { mutableStateOf(false) }
    var ageInRange by remember { mutableStateOf(false) }

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
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(value = schoolName, onValueChange = { schoolName = it }, label = { Text("School Name") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp))
                    }
                }
            }
            item {
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Documents", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        ChecklistItem(label = "Birth Certificate Provided", checked = birthCertificate, onCheckedChange = { birthCertificate = it })
                        ChecklistItem(label = "Death Certificate of Parent(s) Provided", checked = deathCertificate, onCheckedChange = { deathCertificate = it })
                        ChecklistItem(label = "Guardian Consent Form Provided", checked = guardianConsent, onCheckedChange = { guardianConsent = it })
                        ChecklistItem(label = "Health Record Available", checked = healthRecord, onCheckedChange = { healthRecord = it })
                        ChecklistItem(label = "Age within 1–18 years", checked = ageInRange, onCheckedChange = { ageInRange = it })
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
                        if (schoolName.isBlank()) missingFields.add("School Name")
                        if (!birthCertificate) missingFields.add("Birth Certificate")
                        if (!deathCertificate) missingFields.add("Death Certificate")
                        if (!guardianConsent) missingFields.add("Guardian Consent")
                        if (!healthRecord) missingFields.add("Health Record")
                        if (!ageInRange) missingFields.add("Age in Range")

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
                                birthCertificate = birthCertificate,
                                deathCertificate = deathCertificate,
                                guardianConsent = guardianConsent,
                                healthRecord = healthRecord,
                                ageInRange = ageInRange,
                                status = "Active"
                            )
                            viewModel.addOrphan(newOrphan)
                            onEnrollmentSuccess(newOrphan.id!!)
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
