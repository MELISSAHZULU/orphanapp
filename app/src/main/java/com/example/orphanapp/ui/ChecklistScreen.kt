package com.example.orphanapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistScreen(navController: NavController) {
    var applicantName by remember { mutableStateOf("") }
    var inquirerName by remember { mutableStateOf("") }
    var presentAddress by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var postOffice by remember { mutableStateOf("") }
    var policeStation by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var motherName by remember { mutableStateOf("") }
    var guardianName by remember { mutableStateOf("") }
    var guardianRelationship by remember { mutableStateOf("") }
    var guardianOccupation by remember { mutableStateOf("") }
    var witnessName by remember { mutableStateOf("") }
    var villageHeadName by remember { mutableStateOf("") }
    var villageHeadAddress by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checklist") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4CAF50),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController, "checklist")
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            item { ChecklistTextField(label = "Name of Applicant", value = applicantName, onValueChange = { applicantName = it }) }
            item { ChecklistTextField(label = "Name of the Person Who Inquired", value = inquirerName, onValueChange = { inquirerName = it }) }
            item { ChecklistTextField(label = "Present Address", value = presentAddress, onValueChange = { presentAddress = it }) }
            item { ChecklistTextField(label = "Village", value = village, onValueChange = { village = it }) }
            item { ChecklistTextField(label = "Post Office", value = postOffice, onValueChange = { postOffice = it }) }
            item { ChecklistTextField(label = "Police Station", value = policeStation, onValueChange = { policeStation = it }) }
            item { ChecklistTextField(label = "Age", value = age, onValueChange = { age = it }) }
            item { ChecklistTextField(label = "Father's Name", value = fatherName, onValueChange = { fatherName = it }) }
            item { ChecklistFileItem(label = "Father's Death Certificate (Medical)") }
            item { ChecklistTextField(label = "Mother's Name", value = motherName, onValueChange = { motherName = it }) }
            item { ChecklistFileItem(label = "Mother's Death Certificate (Medical)") }
            item { ChecklistTextField(label = "Name of Guardian", value = guardianName, onValueChange = { guardianName = it }) }
            item { ChecklistTextField(label = "Relationship with Guardian", value = guardianRelationship, onValueChange = { guardianRelationship = it }) }
            item { ChecklistTextField(label = "Occupation of Guardian", value = guardianOccupation, onValueChange = { guardianOccupation = it }) }
            item { ChecklistFileItem(label = "Police Verification Report") }
            item { ChecklistTextField(label = "Name of Witness", value = witnessName, onValueChange = { witnessName = it }) }
            item { ChecklistTextField(label = "Name of Village Head", value = villageHeadName, onValueChange = { villageHeadName = it }) }
            item { ChecklistTextField(label = "Village Head Address", value = villageHeadAddress, onValueChange = { villageHeadAddress = it }) }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { ChecklistTextField(label = "Place", value = place, onValueChange = { place = it }) }
            item { ChecklistTextField(label = "Date", value = date, onValueChange = { date = it }) }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.navigate("enrollment") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Submit")
                }
            }
        }
    }
}

@Composable
fun ChecklistTextField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(24.dp)
    )
}

@Composable
fun ChecklistFileItem(label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label)
        Spacer(modifier = Modifier.weight(1f))
        OutlinedButton(onClick = { /* Handle file attachment */ }) {
            Text("Attach File")
        }
    }
}
