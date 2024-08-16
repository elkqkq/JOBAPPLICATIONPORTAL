package com.example.jobapplicationportal.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private lateinit var auth: FirebaseAuth

@Composable
fun LoginScreen() {
    // Initialize Firebase Auth
    auth = FirebaseAuth.getInstance()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Create an Account",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium,
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Login Avatar",
                    modifier = Modifier.size(64.dp),
                    tint = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Forgot Password?",
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { signUp(email, password, { user -> successMessage = "Welcome ${user?.email}" }, { error -> errorMessage = error }) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign Up")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display error or success message
                errorMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                successMessage?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

private fun signUp(email: String, password: String, onSuccess: (FirebaseUser?) -> Unit, onFailure: (String) -> Unit) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSuccess(auth.currentUser)
            } else {
                onFailure(task.exception?.message ?: "Sign up failed")
            }
        }
}