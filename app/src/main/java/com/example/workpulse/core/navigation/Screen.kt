package com.example.workpulse.core.navigation

import okhttp3.Route

sealed class Screen(val route : String){
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object Login : Screen("login")
    data object Profile : Screen("profile")

    data object AttendanceHistory : Screen("attendance_history")

    data object LeaveApplication : Screen("leave_application")
}