package com.example.jobapplicationportal.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobapplicationportal.model.Job
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SharedViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // StateFlow for job list
    private val _jobList = MutableStateFlow<List<Job>>(emptyList())
    val jobList: StateFlow<List<Job>> get() = _jobList

    // State for login status
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState

    // Login user (both admin and regular user)
    fun loginUser(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        _loginState.value = LoginState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                _loginState.value = LoginState.Success
                onSuccess()
            }
            .addOnFailureListener {
                _loginState.value = LoginState.Error(it.message ?: "Unknown error")
                onFailure(it.message ?: "Unknown error")
            }
    }

    // Signup user (both admin and regular user)
    fun signupUser(email: String, password: String, isAdmin: Boolean, onSuccess: () -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val userId = result.user?.uid ?: return@addOnSuccessListener
                val userData = hashMapOf(
                    "email" to email,
                    "isAdmin" to isAdmin
                )
                firestore.collection("users").document(userId).set(userData)
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener {
                        println("Error adding user data: ${it.message}")
                    }
            }
            .addOnFailureListener {
                println("Signup error: ${it.message}")
            }
    }

    // Reset password
    fun resetPassword(email: String, onResult: (String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                onResult("Password reset email sent. Please check your inbox.")
            }
            .addOnFailureListener { exception ->
                onResult("Error: ${exception.message}")
            }
    }

    // Fetch jobs from Firestore
    fun fetchJobs() {
        firestore.collection("jobs")
            .get()
            .addOnSuccessListener { result ->
                val jobs = result.map { document ->
                    document.toObject(Job::class.java).copy(id = document.id)
                }
                _jobList.value = jobs
            }
            .addOnFailureListener { exception ->
                println("Error fetching jobs: ${exception.message}")
            }
    }

    // Add job to Firestore (Admin only)
    fun addJob(title: String, companyName: String, location: String, description: String, onSuccess: () -> Unit) {
        val newJob = hashMapOf(
            "title" to title,
            "companyName" to companyName,
            "location" to location,
            "description" to description
        )
        firestore.collection("jobs")
            .add(newJob)
            .addOnSuccessListener {
                onSuccess()
                fetchJobs() // Refresh job list after adding a new job
            }
            .addOnFailureListener { exception ->
                println("Error adding job: ${exception.message}")
            }
    }

    // Delete job from Firestore (Admin only)
    fun deleteJob(job: Job) {
        firestore.collection("jobs").document(job.id)
            .delete()
            .addOnSuccessListener {
                fetchJobs() // Refresh job list after deletion
            }
            .addOnFailureListener { exception ->
                println("Error deleting job: ${exception.message}")
            }
    }

    // Apply for a job (User only)
    fun applyForJob(job: Job) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val applicationData = hashMapOf(
                "userId" to currentUser.uid,
                "jobId" to job.id,
                "jobTitle" to job.title,
                "companyName" to job.companyName,
                "applicationDate" to System.currentTimeMillis()
            )
            firestore.collection("applications")
                .add(applicationData)
                .addOnSuccessListener {
                    println("Successfully applied for job: ${job.title}")
                }
                .addOnFailureListener { exception ->
                    println("Error applying for job: ${exception.message}")
                }
        } else {
            println("Error: User not logged in.")
        }
    }

    // LoginState sealed class to manage login state
    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
        data class Error(val error: String) : LoginState()
    }
}
