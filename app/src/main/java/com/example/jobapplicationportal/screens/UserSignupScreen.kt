package com.example.jobapplicationportal.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobapplicationportal.utils.SharedViewModel
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSignupScreen(
    navController: NavController,
    viewModel: SharedViewModel<Any>
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isSignUpSuccessful by remember { mutableStateOf(false) }
    var showVerificationPrompt by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(false) }

    // Navigate to login screen if signup is successful
    LaunchedEffect(isSignUpSuccessful) {
        if (isSignUpSuccessful) {
            navController.navigate("user_login") {
                popUpTo("user_signup") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("User Signup") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
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
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = rememberMe,
                    onCheckedChange = { rememberMe = it }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Remember me")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (showVerificationPrompt) {
                Text(
                    text = "Please check your email to verify your account.",
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (password == confirmPassword) {
                        viewModel.signupUser(
                            email = email,
                            password = password,
                            isAdmin = false,
                            onSuccess = {
                                val auth = FirebaseAuth.getInstance()
                                auth.currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        showVerificationPrompt = true
                                        isSignUpSuccessful = true // Set the flag to true if sign up and email verification succeeds
                                    } else {
                                        errorMessage = task.exception?.message ?: "Failed to send verification email"
                                    }
                                }
                            },
                            onFailure = {
                                errorMessage = it // Display the error message if sign up fails
                            }
                        )
                    } else {
                        errorMessage = "Passwords do not match"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Up")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Link for users who already have an account
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Already have an account? ",
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Login",
                    color = MaterialTheme.colorScheme.primary,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        navController.navigate("user_login")
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // "Forgot Password?" Link
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
                        navController.navigate("forgot_password_screen")
                    }
                )
            }
        }
    }
}
