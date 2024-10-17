package com.example.jobapplicationportal.model

import java.util.Date

data class Notification(
    val id: String = "", // Unique identifier for the notification
    val text: String = "", // The notification content/message
    val date: Date = Date(), // Date when the notification was created
    val isRead: Boolean = false // Flag to track whether the notification has been read
)
