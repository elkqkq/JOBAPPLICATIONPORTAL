package com.example.jobapplicationportal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jobapplicationportal.screens.*
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
    NavGraph(navController = navController, viewModel = viewModel)
}

@Composable
fun NavGraph(navController: NavHostController, viewModel: SharedViewModel) {
    NavHost(navController = navController, startDestination = PointerEventPass.Main.route) {
        composable(Screen.Main.route) {
            MainScreen(navController = navController)
        }
        composable(Screen.UserLogin.route) {
            UserLoginScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.AdminLogin.route) {
            AdminLoginScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.UserDashboard.route) {
            UserDashboardScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.AdminDashboard.route) {
            AdminDashboardScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
        composable(Screen.AddData.route) {
            AddDataScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.GetData.route) {
            GetDataScreen(navController = navController, viewModel = viewModel, isAdmin = false)
        }
        composable(Screen.UserSignup.route) {
            UserSignupScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.AdminSignup.route) {
            AdminSignupScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screen.ForgotPassword.route) {
            ForgetPasswordScreen(navController = navController, viewModel = viewModel)
        }
    }
}
