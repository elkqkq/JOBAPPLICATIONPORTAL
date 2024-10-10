package com.example.jobapplicationportal.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobapplicationportal.model.Job
import com.example.jobapplicationportal.utils.SharedViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GetDataScreen(
    navController: NavController,
    viewModel: SharedViewModel<Any?>,
    isAdmin: Boolean
) {
    val jobList by viewModel.jobList.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Available Jobs") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            items(jobList) { job ->
                JobCard(
                    job = job,
                    isAdmin = isAdmin,
                    onEdit = { if (isAdmin) navController.navigate("edit_job/${job.id}") },
                    onDelete = { if (isAdmin) viewModel.deleteJob(job) },
                    onApply = { if (!isAdmin) viewModel.applyForJob(job) }
                )
            }
        }
    }
}

@Composable
fun JobCard(
    job: Job,
    isAdmin: Boolean,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    onApply: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = job.title, style = MaterialTheme.typography.titleLarge)
            Text(text = job.companyName, style = MaterialTheme.typography.bodyMedium)
            Text(text = job.location, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

            if (isAdmin) {
                Row {
                    Button(onClick = onEdit) { Text("Edit") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onDelete) { Text("Delete") }
                }
            } else {
                Button(onClick = onApply) { Text("Apply") }
            }
        }
    }
}
