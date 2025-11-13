package com.example.orphanapp.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChecklistScreen(navController: NavController) {
    var childName by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var placeOfBirth by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var ta by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var guardian1 by remember { mutableStateOf("") }
    var relationship1 by remember { mutableStateOf("") }
    var guardian2 by remember { mutableStateOf("") }
    var relationship2 by remember { mutableStateOf("") }
    var orphanStatus by remember { mutableStateOf("") }
    var motherDeath by remember { mutableStateOf("") }
    var fatherDeath by remember { mutableStateOf("") }
    var siblings by remember { mutableStateOf("") }
    var health by remember { mutableStateOf("") }
    var medicalConditions by remember { mutableStateOf("") }
    var disabilities by remember { mutableStateOf("") }
    var circumstances by remember { mutableStateOf("") }
    var religiousAffiliation by remember { mutableStateOf("") }
    var churchOrMosque by remember { mutableStateOf("") }
    var isBornAgain by remember { mutableStateOf(false) }
    var bornAgainDate by remember { mutableStateOf("") }
    var testimony by remember { mutableStateOf("") }
    var schoolName by remember { mutableStateOf("") }
    var stdForm by remember { mutableStateOf("") }
    var favoriteSubject by remember { mutableStateOf("") }
    var favoriteColor by remember { mutableStateOf("") }
    var favoriteFood by remember { mutableStateOf("") }
    var favoriteActivity by remember { mutableStateOf("") }
    var futureDreams by remember { mutableStateOf("") }
    var recommendation by remember { mutableStateOf(false) }
    var program by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var staffName by remember { mutableStateOf("") }
    var signatureDate by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Child Intake Form") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
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
            item { SectionTitle("CHILD HISTORY") }
            item { ChecklistTextField(label = "Name of Child", value = childName, onValueChange = { childName = it }) }
            item { Row {
                ChecklistTextField(label = "Date of Birth: DD/MM/YYYY", value = dob, onValueChange = { dob = it }, modifier = Modifier.weight(1f))
                ChecklistTextField(label = "Place of Birth", value = placeOfBirth, onValueChange = { placeOfBirth = it }, modifier = Modifier.weight(1f))
            } }
            item { Row {
                ChecklistTextField(label = "Village and T/A", value = village, onValueChange = { village = it }, modifier = Modifier.weight(1f))
                ChecklistTextField(label = "District", value = district, onValueChange = { district = it }, modifier = Modifier.weight(1f))
            } }
            item { Row {
                ChecklistTextField(label = "Guardian", value = guardian1, onValueChange = { guardian1 = it }, modifier = Modifier.weight(1f))
                ChecklistTextField(label = "Relationship", value = relationship1, onValueChange = { relationship1 = it }, modifier = Modifier.weight(1f))
            } }
            item { Row {
                ChecklistTextField(label = "Guardian", value = guardian2, onValueChange = { guardian2 = it }, modifier = Modifier.weight(1f))
                ChecklistTextField(label = "Relationship", value = relationship2, onValueChange = { relationship2 = it }, modifier = Modifier.weight(1f))
            } }
            item { SectionTitle("Family") }
            item { ChecklistTextField(label = "Orphan Status", value = orphanStatus, onValueChange = { orphanStatus = it }) }
            item { ChecklistTextField(label = "Mother: (year and cause of death, if known)", value = motherDeath, onValueChange = { motherDeath = it }) }
            item { ChecklistTextField(label = "Father: (year and cause of death, if known)", value = fatherDeath, onValueChange = { fatherDeath = it }) }
            item { ChecklistTextField(label = "Siblings/relatives at Passion (full name and relationship)", value = siblings, onValueChange = { siblings = it }) }
            item { SectionTitle("Health") }
            item { ChecklistTextField(label = "Major, on-going illnesses (e.g. HIV, TB, diabetes, etc.)", value = health, onValueChange = { health = it }) }
            item { ChecklistTextField(label = "Other medical conditions (e.g. hearing/vision loss, malnourishment, etc.)", value = medicalConditions, onValueChange = { medicalConditions = it }) }
            item { ChecklistTextField(label = "Other disabilities (e.g. hearing/vision loss, malnourishment, etc.)", value = disabilities, onValueChange = { disabilities = it }) }
            item { PhotoAttachment() }
            item { ChecklistTextField(label = "Summary of Circumstances (Story)", value = circumstances, onValueChange = { circumstances = it }, singleLine = false, minLines = 5) }
            item { SectionTitle("Spiritual Background") }
            item { ChecklistTextField(label = "Religious Affiliation", value = religiousAffiliation, onValueChange = { religiousAffiliation = it }) }
            item { ChecklistTextField(label = "Church or Mosque", value = churchOrMosque, onValueChange = { churchOrMosque = it }) }
            item { Row(verticalAlignment = Alignment.CenterVertically) {
                Text("If Christian, do they profess to be born again?")
                Checkbox(checked = isBornAgain, onCheckedChange = { isBornAgain = it })
                ChecklistTextField(label = "Date", value = bornAgainDate, onValueChange = { bornAgainDate = it })
            } }
            item { ChecklistTextField(label = "Child’s Testimony (How they came to Christ)", value = testimony, onValueChange = { testimony = it }, singleLine = false, minLines = 3) }
            item { SectionTitle("STUDENT PROFILE") }
            item { Row {
                ChecklistTextField(label = "Name of School", value = schoolName, onValueChange = { schoolName = it }, modifier = Modifier.weight(1f))
                ChecklistTextField(label = "Std/Form", value = stdForm, onValueChange = { stdForm = it }, modifier = Modifier.weight(1f))
            } }
            item { ChecklistTextField(label = "What is their favorite subject in school?", value = favoriteSubject, onValueChange = { favoriteSubject = it }) }
            item { ChecklistTextField(label = "What is their favorite color?", value = favoriteColor, onValueChange = { favoriteColor = it }) }
            item { ChecklistTextField(label = "What is their favorite food?", value = favoriteFood, onValueChange = { favoriteFood = it }) }
            item { ChecklistTextField(label = "What is their favorite activity?", value = favoriteActivity, onValueChange = { favoriteActivity = it }) }
            item { ChecklistTextField(label = "What dreams do they have for the future?", value = futureDreams, onValueChange = { futureDreams = it }) }
            item { SectionTitle("RECOMMENDATIONS") }
            item { Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Do you recommend this child be in the Passion Center program?")
                Checkbox(checked = recommendation, onCheckedChange = { recommendation = it })
            } }
            item { Text("If yes, tick the appropriate Program:") }
            item { Column {
                val programs = listOf("Residency", "Village-Mulunguzi", "Village – Jali", "Child-headed Household (CHH)", "Infant Rescue Home", "Champions Club")
                programs.forEach { programOption ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = program == programOption, onClick = { program = programOption })
                        Text(programOption)
                    }
                }
            } }
            item { ChecklistTextField(label = "Explain why this child should be admitted to the Passion Center program:", value = reason, onValueChange = { reason = it }, singleLine = false, minLines = 4) }
            item { Row {
                ChecklistTextField(label = "Name of Staff", value = staffName, onValueChange = { staffName = it }, modifier = Modifier.weight(1f))
                ChecklistTextField(label = "Date", value = signatureDate, onValueChange = { signatureDate = it }, modifier = Modifier.weight(1f))
            } }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item {
                Button(
                    onClick = { navController.navigate("enrollment") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit")
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun ChecklistTextField(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier, singleLine: Boolean = true, minLines: Int = 1) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        singleLine = singleLine,
        minLines = minLines
    )
}

@Composable
fun PhotoAttachment() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        imageUri = it
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Selected image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Insert Child’s Photo here")
                    Spacer(modifier = Modifier.height(8.dp))
                    IconButton(onClick = { launcher.launch("image/*") }) {
                        Icon(Icons.Default.AddAPhoto, contentDescription = "Add Photo", modifier = Modifier.size(48.dp))
                    }
                }
            }
        }
    }
}
