package com.example.jobapplicationportal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.jobapplicationportal.nav.NavGraph
import com.example.jobapplicationportal.utils.SharedViewModel

class MainActivity : ComponentActivity() {
    private val sharedViewModel = SharedViewModel() // ViewModel for handling data operations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JobPortalApp(sharedViewModel)
        }
    }
}

@Composable
fun JobPortalApp(viewModel: SharedViewModel) {
    val navController = rememberNavController()
    NavGraph(navController = navController, sharedViewModel = viewModel)
}
