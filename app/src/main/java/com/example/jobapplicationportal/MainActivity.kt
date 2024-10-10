package com.example.jobapplicationportal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.jobapplicationportal.nav.NavGraph
import com.example.jobapplicationportal.utils.SharedViewModel
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    private val sharedViewModel = SharedViewModel<Any>() // ViewModel for handling data operations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase once
        FirebaseApp.initializeApp(this)

        setContent {
            JobPortalApp(sharedViewModel)
        }
    }
}

@Composable
fun JobPortalApp(viewModel: SharedViewModel<Any?>) {
    val navController = rememberNavController()
    NavGraph(navController = navController, sharedViewModel = viewModel)
}
