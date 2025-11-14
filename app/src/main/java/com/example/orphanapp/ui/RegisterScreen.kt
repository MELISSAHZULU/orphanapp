package com.example.orphanapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.orphanapp.viewmodel.AuthState
import com.example.orphanapp.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var userRole by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val roles = listOf("Village Head", "Health Worker", "Orphanage Staff", "Admin")

    val authState by authViewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(authState) {
        (authState as? AuthState.Error)?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Register", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onPrimary)
                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = PasswordVisualTransformation(),
                            shape = RoundedCornerShape(24.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                            OutlinedTextField(
                                value = userRole,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("User Role") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = RoundedCornerShape(24.dp)
                            )
                            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                roles.forEach { role ->
                                    DropdownMenuItem(text = { Text(role) }, onClick = {
                                        userRole = role
                                        expanded = false
                                    })
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = { authViewModel.register(email, password) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("Register", modifier = Modifier.padding(8.dp))
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Already have an account? ")
                            ClickableText(
                                text = AnnotatedString("Login"),
                                onClick = { offset -> navController.navigate("login") },
                                style = TextStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}
