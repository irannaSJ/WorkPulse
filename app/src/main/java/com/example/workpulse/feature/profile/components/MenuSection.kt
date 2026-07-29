package com.example.workpulse.feature.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun MenuSection(

    onAttendanceClick: () -> Unit,

    onLeaveClick : () -> Unit,

    onSettingsClick: () -> Unit,

    onLogoutClick: () -> Unit

) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(24.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(4.dp)

    ) {

        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {

            MenuItem(
                icon = Icons.Outlined.Badge,
                iconColor = Color(0xFFF59E0B),
                title = "Attendance History",
                subtitle = "View attendance records",
                onClick = onAttendanceClick
            )

            HorizontalDivider()

            MenuItem(
                icon = Icons.Outlined.EventAvailable,
                iconColor = Color(0xFF8B5CF6),
                title = "Leave Management",
                subtitle = "Check leave balance and requests",
                onClick = onLeaveClick
            )
//
//            HorizontalDivider()
//
//            MenuItem(
//                icon = Icons.Outlined.Settings,
//                iconColor = Color(0xFF6B7280),
//                title = "Settings",
//                subtitle = "Application preferences",
//                onClick = onSettingsClick
//            )

            HorizontalDivider()

            MenuItem(
                icon = Icons.Outlined.Logout,
                iconColor = Color(0xFFEF4444),
                title = "Logout",
                subtitle = "Sign out from WorkPulse",
                onClick = onLogoutClick
            )

        }

    }

}

@Composable
private fun MenuItem(

    icon: ImageVector,

    iconColor: Color,

    title: String,

    subtitle: String,

    onClick: () -> Unit

) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),

        verticalAlignment = Alignment.CenterVertically

    ) {

        Box(

            modifier = Modifier
                .size(48.dp)
                .background(
                    iconColor.copy(alpha = 0.12f),
                    CircleShape
                ),

            contentAlignment = Alignment.Center

        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor
            )

        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

        }

        Icon(
            imageVector = Icons.Outlined.ChevronRight,
            contentDescription = null,
            tint = Color.Gray
        )

    }

}