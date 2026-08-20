package com.example.workpulse.feature.home.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun AttendanceHistoryCard(
    attendance: AttendanceHistoryUiModel,
    onClick: (AttendanceHistoryUiModel) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                        onClick(attendance)
            }
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.Top
        ) {

            // Date Section
            Column(
                modifier = Modifier.width(95.dp)
            ) {

                Text(
                    text = attendance.date,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = attendance.day,
                    color = MaterialTheme.colorScheme.surface,
                    fontSize = 15.sp
                )

            }

            Spacer(modifier = Modifier.width(10.dp))

            // Punch In
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Outlined.Login,
                        null,
                        tint = Color(0xFF16A34A)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        attendance.punchIn,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )

                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.Top
                ) {

                    Icon(
                        Icons.Default.LocationOn,
                        null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        attendance.punchInLocation,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )

                }

            }

            Spacer(modifier = Modifier.width(12.dp))

            // Punch Out
            Column(
                modifier = Modifier.weight(1f)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Outlined.Logout,
                        null,
                        tint = Color(0xFFEF4444)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        attendance.punchOut,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )

                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.Top
                ) {

                    Icon(
                        Icons.Default.LocationOn,
                        null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        attendance.punchOutLocation,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )

                }

            }

            Spacer(modifier = Modifier.width(12.dp))

            // Working Hours
            Column(
                horizontalAlignment = Alignment.End
            ) {

                Text(
                    attendance.totalHours,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                StatusBadge(
                    attendance.status
                )

            }

        }

    }

}


@Composable
private fun StatusBadge(
    status: AttendanceStatus
) {

    val background = when (status) {

        AttendanceStatus.PRESENT -> Color(0xFFE8F8EC)

        AttendanceStatus.ABSENT -> Color(0xFFFFEBEE)

        AttendanceStatus.HALF_DAY -> Color(0xFFFFF7E0)

    }

    val textColor = when (status) {

        AttendanceStatus.PRESENT -> Color(0xFF16A34A)

        AttendanceStatus.ABSENT -> Color(0xFFDC2626)

        AttendanceStatus.HALF_DAY -> Color(0xFFF59E0B)

    }

    Surface(
        shape = RoundedCornerShape(50),
        color = background
    ) {

        Text(
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 5.dp
            ),
            text = when (status) {

                AttendanceStatus.PRESENT -> "Present"

                AttendanceStatus.ABSENT -> "Absent"

                AttendanceStatus.HALF_DAY -> "Half Day"

            },
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp
        )

    }

}