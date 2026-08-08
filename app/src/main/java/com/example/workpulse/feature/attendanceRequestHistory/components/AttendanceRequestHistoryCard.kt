package com.example.workpulse.feature.attendanceRequestHistory.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workpulse.data.local.entity.AttendanceRequestStatus
import com.example.workpulse.feature.attendanceRequestHistory.AttendanceRequestHistoryCardUi


@Composable
fun AttendanceRequestHistoryCard(
    request: AttendanceRequestHistoryCardUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // --------------------------------
            // Request Type + Status
            // --------------------------------

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = request.requestType.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                AttendanceRequestStatusChip(
                    status = request.status
                )
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            // --------------------------------
            // Date Range
            // --------------------------------

            Text(
                text = "${request.fromDate} → ${request.toDate}",
                style = MaterialTheme.typography.bodyMedium
            )

            // --------------------------------
            // Explanation
            // --------------------------------

            if (request.explanation.isNotBlank()) {

                Text(
                    text = request.explanation,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
            }

            // --------------------------------
            // Include Holidays
            // --------------------------------

            Text(
                text = "Include Holidays: ${
                    if (request.includeHolidays) {
                        "Yes"
                    } else {
                        "No"
                    }
                }",
                style = MaterialTheme.typography.bodySmall
            )

            // --------------------------------
            // Applied Date
            // --------------------------------

            Text(
                text = "Applied: ${request.appliedOn}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}



@Composable
fun AttendanceRequestStatusChip(
    status: AttendanceRequestStatus
) {

    Text(
        text = status.displayName(),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.SemiBold
    )
}


fun AttendanceRequestStatus.displayName(): String {

    return when (this) {

        AttendanceRequestStatus.PENDING ->
            "Pending"

        AttendanceRequestStatus.APPROVED ->
            "Approved"

        AttendanceRequestStatus.REJECTED ->
            "Rejected"

        AttendanceRequestStatus.CANCELLED ->
            "Cancelled"
    }
}