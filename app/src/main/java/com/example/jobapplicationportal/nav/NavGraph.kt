package com.example.jobapplicationportal.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.jobapplicationportal.screens.*
import com.example.jobapplicationportal.utils.SharedViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screens.UserLoginScreen.route
    ) {

        // User Login screen
        composable(route = Screens.UserLoginScreen.route) {
            UserLoginScreen(navController = navController, viewModel = sharedViewModel)
        }

        // User Sign Up screen
        composable(route = Screens.UserSignupScreen.route) {
            UserSignupScreen(navController = navController, viewModel = sharedViewModel)
        }

        // Admin Login screen
        composable(route = Screens.AdminLoginScreen.route) {
            AdminLoginScreen(navController = navController, viewModel = sharedViewModel)
        }

        // Admin Sign Up screen
        composable(route = Screens.AdminSignupScreen.route) {
            AdminSignupScreen(navController = navController, viewModel = sharedViewModel)
        }

        // Forgot Password screen
        composable(route = Screens.ForgotPasswordScreen.route) {
            ForgotPasswordScreen(navController = navController, viewModel = sharedViewModel)
        }

        // Profile screen
        composable(route = Screens.ProfileScreen.route) {
            ProfileScreen(navController = navController, viewModel = sharedViewModel)
        }

        // User Dashboard screen
        composable(route = Screens.UserDashboardScreen.route) {
            UserDashboardScreen(navController = navController, viewModel = sharedViewModel, isAdmin = false)
        }

        // Admin Dashboard screen
        composable(route = Screens.AdminDashboardScreen.route) {
            AdminDashboardScreen(navController = navController, viewModel = sharedViewModel, isAdmin = true)
        }

        // Main screen
        composable(route = Screens.MainScreen.route) {
            MainScreen(navController = navController, viewModel = sharedViewModel)
        }

        // Get Data screen
        composable(route = Screens.GetDataScreen.route) {
            GetDataScreen(navController = navController, viewModel = sharedViewModel, isAdmin = false)
        }

        // Add Data screen
        composable(route = Screens.AddDataScreen.route) {
            AddDataScreen(navController = navController, viewModel = sharedViewModel)
        }

        // Notification screen (optional, if added)
//        composable(route = Screens.NotificationScreen.route) {
//            NotificationScreen(navController = navController, viewModel = sharedViewModel)
//        }
    }
}
