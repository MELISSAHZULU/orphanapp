package com.example.orphanapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.orphanapp.model.Orphan
import com.example.orphanapp.viewmodel.EnrollmentViewModel

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
    var birthCertificate by remember { mutableStateOf(false) }
    var guardianConsent by remember { mutableStateOf(false) }
    var healthRecord by remember { mutableStateOf(false) }
    var ageInRange by remember { mutableStateOf(false) }

    Scaffold(
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                }
            }
            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Documents", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    ChecklistItem(label = "Birth Certificate Provided", checked = birthCertificate, onCheckedChange = { birthCertificate = it })
                    ChecklistItem(label = "Guardian Consent Form Provided", checked = guardianConsent, onCheckedChange = { guardianConsent = it })
                    ChecklistItem(label = "Health Record Available", checked = healthRecord, onCheckedChange = { healthRecord = it })
                    ChecklistItem(label = "Age within 1–18 years", checked = ageInRange, onCheckedChange = { ageInRange = it })
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    val newOrphan = Orphan(
                        id = (viewModel.orphans.value.size + 1),
                        name = name,
                        age = age.toIntOrNull() ?: 0,
                        gender = gender,
                        birthCertificate = birthCertificate,
                        status = "Active"
                    )
                    viewModel.addOrphan(newOrphan)
                    onEnrollmentSuccess(newOrphan.id!!)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enroll")
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
