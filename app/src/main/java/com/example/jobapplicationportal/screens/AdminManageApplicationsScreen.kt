package com.example.jobapplicationportal.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobapplicationportal.utils.SharedViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminManageApplicationsScreen(navController: NavController, viewModel: SharedViewModel<Any?>) {
    val coroutineScope = rememberCoroutineScope()
    var applications by remember { mutableStateOf<List<Map<String, Any>>>(emptyList()) }
    var errorMessage by remember { mutableStateOf("") }

    // Fetch all applications
    LaunchedEffect(Unit) {
        try {
            viewModel.fetchApplications(
                onSuccess = { applications = it },
                onFailure = { errorMessage = it }
            )
        } catch (e: Exception) {
            errorMessage = e.message ?: "Unknown error"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Applications") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {
            items(applications) { application ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Job: ${application["jobTitle"]}")
                        Text(text = "Applicant ID: ${application["userId"]}")
                        Text(text = "Status: ${application["status"]}")
                        Spacer(modifier = Modifier.height(8.dp))

                        Row {
                            Button(onClick = {
                                coroutineScope.launch {
                                    try {
                                        viewModel.updateApplicationStatus(
                                            applicationId = application["id"].toString(),
                                            newStatus = "Accepted",
                                            onSuccess = {},
                                            onFailure = { errorMessage = it }
                                        )
                                    } catch (e: Exception) {
                                        errorMessage = e.message ?: "Unknown error"
                                    }
                                }
                            }) { Text("Accept") }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = {
                                coroutineScope.launch {
                                    try {
                                        viewModel.updateApplicationStatus(
                                            applicationId = application["id"].toString(),
                                            newStatus = "Rejected",
                                            onSuccess = {},
                                            onFailure = { errorMessage = it }
                                        )
                                    } catch (e: Exception) {
                                        errorMessage = e.message ?: "Unknown error"
                                    }
                                }
                            }) { Text("Reject") }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = {
                                coroutineScope.launch {
                                    try {
                                        viewModel.updateApplicationStatus(
                                            applicationId = application["id"].toString(),
                                            newStatus = "Stalled",
                                            onSuccess = {},
                                            onFailure = { errorMessage = it }
                                        )
                                    } catch (e: Exception) {
                                        errorMessage = e.message ?: "Unknown error"
                                    }
                                }
                            }) { Text("Stall") }
                        }
                    }
                }
            }
        }
        if (applications.isEmpty() && errorMessage.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No applications found.")
            }
        }
        if (errorMessage.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}