package com.example.jobapplicationportal.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobapplicationportal.utils.SharedViewModel
import com.example.jobapplicationportal.model.Notification as CustomNotification // Correctly use CustomNotification to avoid conflict

@Composable
fun UserNotificationsScreen(
    navController: NavController,
    viewModel: SharedViewModel<Any> // Ensure correct type
) {
    // Collect notifications as state from the ViewModel
    val notifications by viewModel.getNotifications().collectAsState(initial = emptyList()) // Use CustomNotification type
    val isLoading by remember { mutableStateOf(false) } // Placeholder for loading state

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        when {
            isLoading -> {
                CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            }
            notifications.isEmpty() -> {
                Text("No notifications available.", Modifier.align(Alignment.CenterHorizontally))
            }
            else -> {
                LazyColumn {
                    // Ensure the items in LazyColumn are CustomNotification objects
                    items(notifications) { notification ->
                        NotificationItem(notification = notification) // Pass CustomNotification type
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: CustomNotification) { // Use CustomNotification for the notification parameter
    Column(Modifier.fillMaxWidth().padding(8.dp)) {
        Text(text = notification.text, style = MaterialTheme.typography.bodyLarge)
        HorizontalDivider(Modifier.padding(vertical = 8.dp))
    }
}
