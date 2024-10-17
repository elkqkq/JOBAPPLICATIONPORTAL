package com.example.jobapplicationportal.model

data class Application(
    val id: String,
    val jobId: String,
    val applicantName: String,
    val applicantEmail: String,
    val resumeLink: String
)