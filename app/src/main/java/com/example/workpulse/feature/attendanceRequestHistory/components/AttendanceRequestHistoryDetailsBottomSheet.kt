package com.example.workpulse.feature.attendanceRequestHistory.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workpulse.data.local.entity.AttendanceRequestStatus
import com.example.workpulse.feature.attendanceRequestHistory.AttendanceRequestHistoryCardUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceRequestHistoryDetailsBottomSheet(

    request: AttendanceRequestHistoryCardUi,

    onDismiss: () -> Unit

) {

    ModalBottomSheet(

        onDismissRequest = onDismiss

    ) {

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(
                    rememberScrollState()
                )
                .navigationBarsPadding()
                .padding(
                    horizontal = 20.dp,
                    vertical = 8.dp
                ),

            verticalArrangement =
                Arrangement.spacedBy(16.dp)

        ) {

            Text(

                text = "Attendance Request Details",

                style =
                    MaterialTheme.typography.headlineSmall,

                fontWeight =
                    FontWeight.Bold
            )

            Text(

                text = request.status.displayName(),

                style =
                    MaterialTheme.typography.labelLarge,

                fontWeight =
                    FontWeight.SemiBold
            )

            Divider()

            DetailRow(
                title = "Request Type",
                value = request.requestType.displayName
            )

            DetailRow(
                title = "From Date",
                value = request.fromDate
            )

            DetailRow(
                title = "To Date",
                value = request.toDate
            )

            DetailRow(
                title = "Include Holidays",
                value =
                    if (request.includeHolidays) {
                        "Yes"
                    } else {
                        "No"
                    }
            )

            DetailRow(
                title = "Explanation",
                value = request.explanation
            )

            DetailRow(
                title = "Applied On",
                value = request.appliedOn
            )

            DetailRow(
                title = "ERPNext ID",
                value = request.erpNextId ?: "-"
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Button(

                onClick = onDismiss,

                modifier = Modifier.fillMaxWidth()

            ) {

                Text(
                    text = "Close"
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }
    }
}


@Composable
private fun DetailRow(

    title: String,

    value: String,

    modifier: Modifier = Modifier

) {

    Row(

        modifier = modifier.fillMaxWidth(),

        horizontalArrangement =
            Arrangement.SpaceBetween

    ) {

        Text(

            text = title,

            style =
                MaterialTheme.typography.bodyMedium,

            fontWeight =
                FontWeight.SemiBold
        )

        Text(

            text = value,

            style =
                MaterialTheme.typography.bodyMedium
        )
    }
}