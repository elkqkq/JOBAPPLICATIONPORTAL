package com.example.jobapplicationportal.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobapplicationportal.utils.SharedViewModel
import androidx.compose.runtime.livedata.observeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminLoginScreen(
    navController: NavController,
    viewModel: SharedViewModel<Any?>
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
                is SharedViewModel<Any?>.LoginState.Loading -> CircularProgressIndicator()
                is SharedViewModel<Any?>.LoginState.Error -> {
                    Text("Login Failed: ${(loginState as SharedViewModel<Any?>.LoginState.Error).error}")
                }
                is SharedViewModel<Any?>.LoginState.Success -> {
                    Text("Login Successful!")
                    // Optionally navigate or handle successful login further
                }
                else -> {}
            }
        }
    }
}
