package com.example.workpulse.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun NavigationDrawerContent(

    employeeName: String,

    designation: String,

    onHomeClick: () -> Unit,

    onProfileClick: () -> Unit,

    onLeaveClick: () -> Unit,

    onAttendanceHistoryClick: () -> Unit,

    onSettingsClick: () -> Unit,

    onLogoutClick: () -> Unit,

    selectedRoute: String

) {

    ModalDrawerSheet {

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 24.dp)
        ) {

            DrawerHeader(
                employeeName = employeeName,
                designation = designation
            )

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(12.dp))

            DrawerMenuItem(
                title = "Home",
                icon = Icons.Outlined.Home,
                selected = selectedRoute == "home",
                onClick = onHomeClick
            )

            DrawerMenuItem(
                title = "Profile",
                icon = Icons.Outlined.Person,
                selected = selectedRoute == "profile",
                onClick = onProfileClick
            )

            DrawerMenuItem(
                title = "Leave Management",
                icon = Icons.Outlined.Event,
                selected = selectedRoute == "leave",
                onClick = onLeaveClick
            )

            DrawerMenuItem(
                title = "Attendance History",
                icon = Icons.Outlined.History,
                selected = selectedRoute == "attendance_history",
                onClick = onAttendanceHistoryClick
            )

            DrawerMenuItem(
                title = "Settings",
                icon = Icons.Outlined.Settings,
                selected = selectedRoute == "settings",
                onClick = onSettingsClick
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            HorizontalDivider()

            Spacer(modifier = Modifier.height(12.dp))

            DrawerMenuItem(
                title = "Logout",
                icon = Icons.Outlined.Logout,
                onClick = onLogoutClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "WorkPulse v1.0.0",
                modifier = Modifier.padding(horizontal = 24.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )

        }

    }

}