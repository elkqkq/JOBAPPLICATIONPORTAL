package com.example.jobapplicationportal.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobapplicationportal.model.Job
import com.example.jobapplicationportal.utils.SharedViewModel

@Composable
fun JobListScreen(navController: NavController, viewModel: SharedViewModel<Any>) {
    // Fetch job list from the view model
    val jobs by viewModel.jobList.collectAsState(initial = emptyList())
    var isLoading by remember { mutableStateOf(true) } // Loading state
    var errorMessage by remember { mutableStateOf("") } // Error message state

    LaunchedEffect(Unit) {
        try {
            viewModel.fetchJobs()  // Call the fetch jobs method in the view model
        } catch (e: Exception) {
            errorMessage = "Error fetching jobs: ${e.message}"
        } finally {
            isLoading = false  // Turn off the loading indicator
        }
    }

    // UI for the Job List
    Box(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            // Show a loading indicator while the jobs are being fetched
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (errorMessage.isNotEmpty()) {
            // Display an error message if something went wrong
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (jobs.isEmpty()) {
            // Show a message if no jobs are available
            Text(
                text = "No jobs available.",
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            // Display the list of jobs when fetched successfully
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(jobs) { job ->
                    JobListItem(job, onEditClick = {
                        // Navigate to JobEditScreen with the job ID when the Edit button is clicked
                        navController.navigate("job_edit_screen/${job.id}")
                    })
                }
            }
        }
    }
}

@Composable
fun JobListItem(job: Job, onEditClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = job.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = "Location: ${job.location}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Company: ${job.companyName}", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onEditClick, modifier = Modifier.align(Alignment.End)) {
                Text("Edit")
            }
        }
    }
}
