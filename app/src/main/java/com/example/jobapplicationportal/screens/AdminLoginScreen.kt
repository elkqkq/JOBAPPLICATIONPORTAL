package com.example.jobapplicationportal.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobapplicationportal.utils.SharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminLoginScreen(
    navController: NavController,
    viewModel: SharedViewModel<Any>
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Observing loginState from the ViewModel (using LiveData or StateFlow)
    val loginState by viewModel.loginState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Login") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    viewModel.loginUser(email, password, onSuccess = {
                        // Navigate to Admin Dashboard after successful login
                        navController.navigate("admin_dashboard")
                    }, onFailure = { error ->
                        // Handle login error
                        println("Login failed: $error")
                    })
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (loginState) {
                is SharedViewModel.LoginState.Loading -> CircularProgressIndicator()
                is SharedViewModel.LoginState.Error -> {
                    Text("Login Failed: ${(loginState as SharedViewModel.LoginState.Error).error}")
                }
                is SharedViewModel.LoginState.Success -> {
                    Text("Login Successful!")
                }
                else -> {}
            }

            Spacer(modifier = Modifier.height(16.dp))

            // "Forgot Password" link
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Forgot your password? ",
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Reset Password",
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        navController.navigate("forgot_password_screen") // Navigate to Forgot Password screen
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // "Sign Up" link
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Sign Up",
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        navController.navigate("admin_signup") // Navigate to Admin Sign Up screen
                    }
                )
            }
        }
    }
}
