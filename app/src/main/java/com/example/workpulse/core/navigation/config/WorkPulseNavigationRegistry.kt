package com.example.workpulse.core.navigation.config

import android.graphics.drawable.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.workpulse.core.navigation.Screen

data class WorkPulseNavigationDestination (
    val navigationKey : String,
    val route : String,
    val icon : ImageVector
)

object WorkPulseNavigationRegistry{
    private val destinations = mapOf(
        "HOME" to WorkPulseNavigationDestination(
            navigationKey = "HOME",
            route = Screen.Home.route,
            icon = Icons.Outlined.Home
        ),
        "ATTENDANCE_HISTORY" to WorkPulseNavigationDestination(
            navigationKey = "ATTENDANCE_HISTORY",
            route = Screen.AttendanceHistory.route,
            icon = Icons.Outlined.History
        ),

        "LEAVE_HISTORY" to WorkPulseNavigationDestination(
            navigationKey = "LEAVE_HISTORY",
            route = Screen.LeaveHistory.route,
            icon = Icons.AutoMirrored.Outlined.EventNote
        ),
        "PROFILE" to WorkPulseNavigationDestination(
            navigationKey = "PROFILE",
            route = Screen.Profile.route,
            icon = Icons.Outlined.Person
        )
    )

    fun resolve(
        navigationKey: String
    ): WorkPulseNavigationDestination? {
        return destinations[navigationKey]
    }
}