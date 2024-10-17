package com.example.jobapplicationportal.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.example.jobapplicationportal.model.Job
import com.example.jobapplicationportal.utils.SharedViewModel

@Composable
fun SearchJobsScreen(
    navController: NavController,
    viewModel: SharedViewModel<Any>
) {
    var query by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    val searchResults by viewModel.searchJobs(query, location).collectAsState(initial = emptyList())
    val keyboardController = LocalSoftwareKeyboardController.current
    var isLoading by remember { mutableStateOf(false) }

    Column(Modifier.padding(16.dp)) {
        TextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Job Title") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            }
            searchResults.isEmpty() -> {
                Text("No jobs found.", Modifier.align(Alignment.CenterHorizontally))
            }
            else -> {
                LazyColumn {
                    items(searchResults) { job ->
                        JobItem(job = job)
                    }
                }
            }
        }
    }
}

@Composable
fun JobItem(job: Job) {
    Column(Modifier.padding(8.dp)) {
        Text(text = "Title: ${job.title}", style = MaterialTheme.typography.headlineSmall)
        Text(text = "Location: ${job.location}", style = MaterialTheme.typography.bodyLarge)
        Text(text = "Description: ${job.description}", style = MaterialTheme.typography.bodyMedium)
        HorizontalDivider(Modifier.padding(vertical = 8.dp))
    }
}
