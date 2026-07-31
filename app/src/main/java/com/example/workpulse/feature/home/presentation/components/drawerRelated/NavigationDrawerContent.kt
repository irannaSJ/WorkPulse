package com.example.workpulse.feature.home.presentation.components.drawerRelated

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun NavigationDrawerContent(

    employeeName: String,

    designation: String,
    company : String,

    onHomeClick: () -> Unit,

    onProfileClick: () -> Unit,

    onLeaveClick: () -> Unit,

    onAttendanceHistoryClick: () -> Unit,

    onSettingsClick: () -> Unit,

    onLogoutClick: () -> Unit,

    selectedRoute: String

) {

    ModalDrawerSheet(

        modifier = Modifier
            .width(320.dp)
            .fillMaxHeight(),

        drawerContainerColor = MaterialTheme.colorScheme.surface,

        drawerShape = RoundedCornerShape(
            topEnd = 28.dp,
            bottomEnd = 28.dp
        )

    ) {

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp)
        ) {

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            DrawerHeader(
                employeeName = employeeName,
                designation = designation,
                company = company
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            HorizontalDivider()

            Spacer(
                modifier = Modifier.height(12.dp)
            )

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
                title = "Leave Application",
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

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            DrawerMenuItem(
                title = "Logout",
                icon = Icons.Outlined.Logout,
                onClick = onLogoutClick,
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Version 1.0.0",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

        }

    }

}