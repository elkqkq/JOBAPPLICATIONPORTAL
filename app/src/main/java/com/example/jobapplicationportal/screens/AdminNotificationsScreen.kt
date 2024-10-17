package com.example.jobapplicationportal.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jobapplicationportal.utils.SharedViewModel

@Composable
fun AdminNotificationsScreen(
    navController: NavController,
    viewModel: SharedViewModel<Any>  // Correctly reference `viewModel`
) {
    var notificationText by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        TextField(
            value = notificationText,
            onValueChange = { notificationText = it },
            label = { Text("Notification Message") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.sendNotification(notificationText)  // Correctly reference `viewModel`
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send Notification")
        }
    }
}
