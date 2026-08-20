package com.example.workpulse.feature.attendance

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.WorkHistory
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.workpulse.feature.home.presentation.AttendanceState

import androidx.compose.material.icons.filled.Logout

import androidx.compose.material.icons.outlined.TaskAlt
import com.example.workpulse.feature.home.presentation.formatWorkingTime

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import com.example.workpulse.core.ui.theme.AppColors
import com.example.workpulse.core.ui.theme.Dimens
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.Instant


@Composable
fun AttendanceCard(

    attendanceState: AttendanceState,

    workingSeconds: Long,

    punchInTime: Long?,

    punchOutTime: Long?,

    onAttendanceClick: () -> Unit

) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(Dimens.cardCornerRadius),

        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens.cardElevation
        ),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )

    ) {

        Column(
            modifier = Modifier.padding(24.dp)
        ) {

            AttendanceHeader(
                attendanceState = attendanceState
            )

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedContent(

                targetState = attendanceState,

                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },

                label = "Attendance"

            ) { state ->

                when (state) {

                    AttendanceState.NOT_PUNCHED_IN -> {

                        NotPunchedInContent(
                            onAttendanceClick = onAttendanceClick
                        )

                    }

                    AttendanceState.PUNCHED_IN -> {

                        PunchedInContent(

                            workingSeconds = workingSeconds,

                            punchInTime = punchInTime,

                            onAttendanceClick = onAttendanceClick

                        )

                    }

                    AttendanceState.PUNCHED_OUT -> {

                        PunchedOutContent(

                            workingSeconds = workingSeconds,

                            punchInTime = punchInTime ,
                            punchOutTime = punchOutTime

                        )

                    }

                }

            }

        }

    }

}

@Composable
private fun NotPunchedInContent(
    onAttendanceClick: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Outlined.PlayCircle,
            contentDescription = null,
            modifier = Modifier.size(90.dp),
            tint = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Ready to start your day?",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Tap below to punch in and begin your work.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onAttendanceClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.Success
            )
        ) {

            Icon(
                imageVector = Icons.Default.Login,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Punch In",
                fontWeight = FontWeight.SemiBold
            )

        }

    }

}

@Composable
private fun PunchedInContent(
    workingSeconds: Long,
    punchInTime: Long?,
    onAttendanceClick: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Outlined.WorkHistory,
            contentDescription = null,
            modifier = Modifier.size(90.dp),
            tint = Color(0xFF2563EB)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "You're currently working",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = formatWorkingTime(workingSeconds),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            color = AppColors.Attendance
        )

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        InfoRow(
            label = "Punch In",
            value = formatPunchTime(punchInTime)
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = onAttendanceClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEF4444)
            )
        ) {

            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = null
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Punch Out",
                fontWeight = FontWeight.SemiBold
            )

        }

    }

}

@Composable
private fun PunchedOutContent(
    workingSeconds: Long,
    punchInTime: Long?,
    punchOutTime: Long?
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = Icons.Outlined.TaskAlt,
            contentDescription = null,
            modifier = Modifier.size(90.dp),
            tint = Color(0xFF22C55E)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Work completed for today!",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "SEe you tomorrow",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(20.dp))

        InfoRow(
            label = "Punch In",
            value = formatPunchTime(punchInTime)
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoRow(
            label = "Punch Out",
            value = formatPunchTime(punchOutTime)
        )

        Spacer(modifier = Modifier.height(12.dp))

        InfoRow(
            label = "Total Hours",
            value = formatWorkingTime(workingSeconds)
        )

    }

}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold
        )

    }

}



private val timeFormatter =
    DateTimeFormatter.ofPattern("hh:mm a")

fun formatPunchTime(time: Long?): String {

    if (time == null) return "--"

    return Instant
        .ofEpochMilli(time)
        .atZone(ZoneId.systemDefault())
        .format(timeFormatter)
}