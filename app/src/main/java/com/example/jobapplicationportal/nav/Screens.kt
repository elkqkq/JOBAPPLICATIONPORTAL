package com.example.jobapplicationportal.nav


// Updated Screens Definitions
sealed class Screens(val route: String) {
    object UserLoginScreen : Screens("user_login")
    object UserSignupScreen : Screens("user_signup")
    object AdminLoginScreen : Screens("admin_login")
    object AdminSignupScreen : Screens("admin_signup")
    object ForgotPasswordScreen : Screens("forgot_password_screen")
    object ProfileScreen : Screens("profile_screen")
    object EditProfileScreen : Screens("edit_profile_screen")
    object ChangePasswordScreen : Screens("change_password_screen")
    object UserDashboardScreen : Screens("user_dashboard_screen")
    object AdminDashboardScreen : Screens("admin_dashboard_screen")
    object MainScreen : Screens("main_screen")
    object GetDataScreen : Screens("get_data_screen")
    object AddDataScreen : Screens("add_data_screen")
    object JobDetailsScreen : Screens("job_details/{jobId}") {
        fun createRoute(jobId: String) = "job_details/$jobId"
    }

    object ApplicationScreen : Screens("user_applications_screen")
    object AdminManageApplicationsScreen : Screens("admin_manage_applications_screen")
}