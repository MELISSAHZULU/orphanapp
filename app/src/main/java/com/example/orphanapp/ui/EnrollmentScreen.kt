package com.example.orphanapp.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.orphanapp.data.Orphan
import com.example.orphanapp.viewmodel.AuthState
import com.example.orphanapp.viewmodel.AuthViewModel
import com.example.orphanapp.viewmodel.EnrollmentViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnrollmentScreen(
    navController: NavController,
    viewModel: EnrollmentViewModel,
    authViewModel: AuthViewModel
) {
    var name by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf<Date?>(null) }
    var gender by remember { mutableStateOf("") }
    var guardianName by remember { mutableStateOf("") }
    var schoolName by remember { mutableStateOf("") }
    var orphanStory by remember { mutableStateOf("") }
    var healthRecords by remember { mutableStateOf("") }

    val newlyCreatedOrphanId by viewModel.newlyCreatedOrphanId.collectAsState()
    val authState by authViewModel.authState.collectAsState()
    val user = (authState as? AuthState.Authenticated)?.user

    LaunchedEffect(newlyCreatedOrphanId) {
        newlyCreatedOrphanId?.let {
            navController.navigate("profile/$it") {
                popUpTo("enrollment") { inclusive = true }
            }
            viewModel.onEnrollmentComplete() // Reset the state
        }
    }

    val isFormValid = name.isNotBlank() && dateOfBirth != null && gender.isNotBlank() &&
            guardianName.isNotBlank() && orphanStory.isNotBlank() && healthRecords.isNotBlank() && !user?.organizationId.isNullOrBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Enroll New Orphan") },
                navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
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
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Orphan Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(16.dp))

                        DatePickerField(selectedDate = dateOfBirth, onDateSelected = { dob -> dateOfBirth = dob })

                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Select Gender", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.fillMaxWidth())
                        Row {
                            GenderSelector(label = "Male", selected = gender == "Male", onSelect = { gender = "Male" })
                            GenderSelector(label = "Female", selected = gender == "Female", onSelect = { gender = "Female" })
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(value = guardianName, onValueChange = { guardianName = it }, label = { Text("Guardian Name") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(value = schoolName, onValueChange = { schoolName = it }, label = { Text("School Name (Optional)") }, modifier = Modifier.fillMaxWidth())
                    }
                }
            }
            item {
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Required Information", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(value = orphanStory, onValueChange = { orphanStory = it }, label = { Text("Orphan Story") }, modifier = Modifier.fillMaxWidth().height(120.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(value = healthRecords, onValueChange = { healthRecords = it }, label = { Text("Health Records") }, modifier = Modifier.fillMaxWidth().height(120.dp))
                    }
                }
            }
            item {
                Button(
                    onClick = {
                        val age = dateOfBirth?.let { calculateAge(it) } ?: 0
                        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                        val enrollmentDate = sdf.format(Date())
                        val orgId = user?.organizationId ?: return@Button

                        val newOrphan = Orphan(
                            documentId = "", // Firestore will generate this
                            name = name,
                            age = age,
                            dateOfBirth = dateOfBirth,
                            gender = gender,
                            enrollmentDate = enrollmentDate,
                            status = "Pending",
                            photoUrl = null, // Set a default value
                            organizationId = orgId,
                            healthRecords = healthRecords,
                            orphanStory = orphanStory,
                            donationsReceived = emptyList(), // Set a default value
                            sponsorInfo = "",
                            educationProgress = "",
                            guardianName = guardianName,
                            schoolName = schoolName
                        )
                        viewModel.addOrphan(newOrphan)
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = isFormValid
                ) {
                    Text("Enroll Orphan", style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
fun DatePickerField(selectedDate: Date?, onDateSelected: (Date) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    selectedDate?.let { calendar.time = it }

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    val dateText = selectedDate?.let { dateFormat.format(it) } ?: "Select Date of Birth"

    val datePickerDialog = remember(context) {
        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val newCalendar = Calendar.getInstance().apply {
                    set(selectedYear, selectedMonth, selectedDay)
                }
                onDateSelected(newCalendar.time)
            }, year, month, day
        )
    }

    LaunchedEffect(selectedDate) {
        selectedDate?.let {
            val cal = Calendar.getInstance()
            cal.time = it
            datePickerDialog.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
        }
    }

    OutlinedTextField(
        value = dateText,
        onValueChange = {},
        label = { Text("Date of Birth") },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { datePickerDialog.show() },
        readOnly = true
    )
}

@Composable
fun GenderSelector(label: String, selected: Boolean, onSelect: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = selected, onClick = onSelect)
        Text(label)
    }
}

fun calculateAge(birthDate: Date): Int {
    val dob = Calendar.getInstance()
    dob.time = birthDate
    val today = Calendar.getInstance()
    var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
        age--
    }
    return age
}
