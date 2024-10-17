package com.example.jobapplicationportal.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.example.jobapplicationportal.utils.SharedViewModel
import com.example.jobapplicationportal.model.Job
import kotlinx.coroutines.flow.collect

@Composable
fun JobEditScreen(
    navController: NavController,
    viewModel: SharedViewModel<Any>, // Correct reference to viewModel
    jobId: String
) {
    val jobState = remember { mutableStateOf<Job?>(null) }

    // Launch a coroutine to fetch the job details using the jobId
    LaunchedEffect(jobId) {
        viewModel.getJobById(jobId).collect { job -> // Use viewModel instead of sharedViewModel
            jobState.value = job
        }
    }

    if (jobState.value == null) {
        // Show loading state while job is being fetched
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        jobState.value?.let { job ->
            var title by remember { mutableStateOf(job.title) }
            var description by remember { mutableStateOf(job.description) }
            var location by remember { mutableStateOf(job.location) }

            Column(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Job Title") }
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
                TextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") }
                )
                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    if (title.isNotEmpty() && description.isNotEmpty() && location.isNotEmpty()) {
                        // Update the job with the new details
                        viewModel.updateJob(jobId, title, description, location)

                        // Navigate back to the previous screen after the job is updated
                        navController.popBackStack()
                    } else {
                        // Handle validation error here, e.g., show a Snackbar
                        // You can add a proper error message if necessary
                    }
                }) {
                    Text("Update Job")
                }
            }
        }
    }
}
