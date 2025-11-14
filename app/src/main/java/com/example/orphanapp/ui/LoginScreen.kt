package com.example.orphanapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.orphanapp.repository.FakeAuthRepository
import com.example.orphanapp.ui.theme.OrphanAppTheme
import com.example.orphanapp.viewmodel.AuthState
import com.example.orphanapp.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var userRole by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val roles = listOf("Village Head", "Health Worker", "Orphanage Staff", "Admin")

    val authState by authViewModel.authState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(authState) {
        if (authState is AuthState.Error) {
            scope.launch {
                snackbarHostState.showSnackbar((authState as AuthState.Error).message)
            }
        }
    }

    if (showForgotPasswordDialog) {
        ForgotPasswordDialog(onDismiss = { showForgotPasswordDialog = false }) {
            // Handle password reset logic here, e.g., call a method in AuthViewModel
            showForgotPasswordDialog = false
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
                AppLogo()
                Spacer(modifier = Modifier.height(32.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username / Email") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(image, "toggle password visibility")
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        if (roles.isNotEmpty()) {
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
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { authViewModel.signIn(username, password) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text("Login", modifier = Modifier.padding(8.dp))
                        }
                        TextButton(onClick = { showForgotPasswordDialog = true }) {
                            Text("Forgot Password?", color = MaterialTheme.colorScheme.secondary)
                        }
                        Row {
                            Text("Don't have an account? ")
                            ClickableText(
                                text = AnnotatedString("Register"),
                                onClick = { offset -> navController.navigate("register") },
                                style = TextStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ForgotPasswordDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Forgot Password") },
        text = { Text("This functionality is not yet implemented.") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("OK")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    OrphanAppTheme {
        val fakeAuthRepository = FakeAuthRepository()
        val authViewModel = AuthViewModel(fakeAuthRepository)
        val navController = rememberNavController()
        LoginScreen(navController = navController, authViewModel = authViewModel)
    }
}
