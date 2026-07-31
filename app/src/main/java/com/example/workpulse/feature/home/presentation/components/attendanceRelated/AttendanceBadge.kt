package com.example.workpulse.feature.home.presentation.components.attendanceRelated

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.workpulse.feature.home.presentation.AttendanceState

@Composable
fun AttendanceBadge(
    attendanceState: AttendanceState
) {

    val (text, color) = when (attendanceState) {

        AttendanceState.NOT_PUNCHED_IN ->
            "Not Punched In" to Color(0xFF22C55E)

        AttendanceState.PUNCHED_IN ->
            "Working" to Color(0xFF2563EB)

        AttendanceState.PUNCHED_OUT ->
            "Completed" to Color(0xFFFF9800)

    }

    AssistChip(

        onClick = {},

        label = {
            Text(text)
        },

        colors = AssistChipDefaults.assistChipColors(
            containerColor = color.copy(alpha = .12f),
            labelColor = color
        ),

        shape = RoundedCornerShape(50)

    )

}