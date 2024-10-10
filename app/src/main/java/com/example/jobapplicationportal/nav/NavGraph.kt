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
    sharedViewModel: SharedViewModel<Any?>
) {
    NavHost(
        navController = navController,
        startDestination = Screens.UserSignupScreen.route
    ) {
        composable(route = Screens.UserLoginScreen.route) {
            UserLoginScreen(navController = navController, viewModel = sharedViewModel)
        }
        composable(route = Screens.UserSignupScreen.route) {
            UserSignupScreen(navController = navController, viewModel = sharedViewModel)
        }
        composable(route = Screens.AdminLoginScreen.route) {
            AdminLoginScreen(navController = navController, viewModel = sharedViewModel)
        }
        composable(route = Screens.AdminSignupScreen.route) {
            AdminSignupScreen(navController = navController, viewModel = sharedViewModel)
        }
        composable(route = Screens.ForgotPasswordScreen.route) {
            ForgotPasswordScreen(navController = navController, viewModel = sharedViewModel)
        }
        composable(route = Screens.ProfileScreen.route) {
            ProfileScreen(navController = navController, viewModel = sharedViewModel)
        }
        composable(route = Screens.EditProfileScreen.route) {
            EditProfileScreen(navController = navController, viewModel = sharedViewModel)
        }
        composable(route = Screens.ChangePasswordScreen.route) {
            ChangePasswordScreen(navController = navController, viewModel = sharedViewModel)
        }
        composable(route = Screens.UserDashboardScreen.route) {
            UserDashboardScreen(navController = navController, viewModel = sharedViewModel, isAdmin = false)
        }
        composable(route = Screens.AdminDashboardScreen.route) {
            AdminDashboardScreen(navController = navController, viewModel = sharedViewModel, isAdmin = true)
        }
        composable(route = Screens.MainScreen.route) {
            MainScreen(navController = navController, viewModel = sharedViewModel)
        }
        composable(route = Screens.GetDataScreen.route) {
            GetDataScreen(navController = navController, viewModel = sharedViewModel, isAdmin = false)
        }
        composable(route = Screens.AddDataScreen.route) {
            AddDataScreen(navController = navController, viewModel = sharedViewModel)
        }
        composable(route = Screens.JobDetailsScreen.route) { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId") ?: return@composable
            JobDetailsScreen(navController = navController, viewModel = sharedViewModel, jobId = jobId)
        }
        composable(route = Screens.ApplicationScreen.route) {
            ApplicationScreen(navController = navController, viewModel = sharedViewModel)
        }
        composable(route = Screens.AdminManageApplicationsScreen.route) {
            AdminManageApplicationsScreen(navController = navController, viewModel = sharedViewModel)
        }
    }
}

