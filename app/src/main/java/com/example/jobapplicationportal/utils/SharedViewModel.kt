package com.example.jobapplicationportal.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobapplicationportal.model.Job
import com.example.jobapplicationportal.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.EmailAuthProvider
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
    fun signupUser(email: String, password: String, isAdmin: Boolean, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
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
                        onFailure("Error adding user data: \${it.message}")
                    }
            }
            .addOnFailureListener {
                onFailure("Signup error: \${it.message}")
            }
    }

    // Reset password
    fun resetPassword(email: String, onResult: (String) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                onResult("Password reset email sent. Please check your inbox.")
            }
            .addOnFailureListener { exception ->
                onResult("Error: \${exception.message}")
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
                println("Error fetching jobs: \${exception.message}")
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
                println("Error adding job: \${exception.message}")
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
                println("Error deleting job: \${exception.message}")
            }
    }

    // Apply for a job (User only)
    fun applyForJob(job: Job, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
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
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    onFailure(exception.message ?: "Error applying for job")
                }
        } else {
            onFailure("User not logged in.")
        }
    }


    // Fetch job details by ID
    fun fetchJobDetails(jobId: String, onSuccess: (Job) -> Unit, onFailure: (String) -> Unit) {
        firestore.collection("jobs").document(jobId)
            .get()
            .addOnSuccessListener { document ->
                val job = document.toObject(Job::class.java)
                if (job != null) {
                    onSuccess(job)
                } else {
                    onFailure("Job not found")
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Unknown error")
            }
    }

    // Fetch user's applications
    fun fetchUserApplications(onSuccess: (List<Map<String, Any>>) -> Unit, onFailure: (String) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection("applications")
                .whereEqualTo("userId", currentUser.uid)
                .get()
                .addOnSuccessListener { result ->
                    val applications = result.documents.map { document ->
                        document.data ?: emptyMap()
                    }
                    onSuccess(applications)
                }
                .addOnFailureListener { exception ->
                    onFailure(exception.message ?: "Error fetching applications")
                }
        } else {
            onFailure("User not logged in.")
        }
    }

    // Manage applications for admins
    fun fetchApplications(onSuccess: (List<Map<String, Any>>) -> Unit, onFailure: (String) -> Unit) {
        firestore.collection("applications")
            .get()
            .addOnSuccessListener { result ->
                val applications = result.documents.map { document ->
                    document.data ?: emptyMap()
                }
                onSuccess(applications)
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Error fetching applications")
            }
    }

    // Update application status
    fun updateApplicationStatus(applicationId: String, newStatus: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        firestore.collection("applications").document(applicationId)
            .update("status", newStatus)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception.message ?: "Error updating application status")
            }
    }

    // Update user profile
    fun updateProfile(name: String, email: String, phoneNumber: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            val updatedData = mapOf(
                "name" to name,
                "email" to email,
                "phoneNumber" to phoneNumber
            )
            firestore.collection("users").document(userId)
                .update(updatedData)
                .addOnSuccessListener {
                    onSuccess()
                }
                .addOnFailureListener { exception ->
                    onFailure(exception.message ?: "Error updating profile")
                }
        } else {
            onFailure("User not logged in.")
        }
    }

    // Fetch current user details
    fun fetchCurrentUser(onSuccess: (UserProfile) -> Unit, onFailure: (String) -> Unit) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid
            firestore.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    val userProfile = document.toObject(UserProfile::class.java)
                    if (userProfile != null) {
                        onSuccess(userProfile)
                    } else {
                        onFailure("User profile not found.")
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure(exception.message ?: "Error fetching user details.")
                }
        } else {
            onFailure("User not logged in.")
        }
    }

    // Change user password
    fun changeUserPassword(newPassword: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        auth.currentUser?.updatePassword(newPassword)
            ?.addOnSuccessListener {
                onSuccess()
            }
            ?.addOnFailureListener { exception ->
                onFailure(exception.message ?: "Error changing password")
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
