package com.example.jobapplicationportal.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobapplicationportal.utils.SharedViewModel
import androidx.compose.runtime.collectAsState

@Composable
fun JobApplicationsListScreen(
    navController: NavController,
    viewModel: SharedViewModel<Any>,
    jobId: String
) {
    // Collect the applications from the viewModel using the jobId
    val applicationsState = viewModel.getApplicationsByJobId(jobId).collectAsState(initial = emptyList())
    val applications = applicationsState.value

    if (applications.isEmpty()) {
        // Show a message when there are no applications
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No applications found or loading...")
        }
    } else {
        LazyColumn(Modifier.padding(16.dp)) {
            items(applications) { application ->
                Column(Modifier.padding(8.dp)) {
                    Text("Applicant Name: ${application.applicantName}")
                    Text("Email: ${application.applicantEmail}")
                    Text("Resume: ${application.resumeLink}")
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider()
                }
            }
        }
    }
}