package com.example.workpulse.feature.home.presentation.history


import androidx.benchmark.traceprocessor.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Login
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import okhttp3.internal.http2.Header

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceDetailsBottomSheet(

    attendance: AttendanceHistoryUiModel,

    onDismiss: () -> Unit,

    sheetState: SheetState = rememberModalBottomSheetState()

) {

    ModalBottomSheet(

        onDismissRequest = onDismiss,

        sheetState = sheetState,

        dragHandle = null

    ) {

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()

        ) {

            Header(onDismiss)

            HorizontalDivider()

            AttendanceSection(attendance)

        }

    }

}


@Composable
private fun Header(

    onDismiss: () -> Unit

) {

    androidx.compose.foundation.layout.Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 12.dp
            )

    ) {

        Text(

            text = "Attendance Details",

            style = MaterialTheme.typography.headlineSmall,

            fontWeight = FontWeight.Bold,

            modifier = Modifier.weight(1f)

        )

        IconButton(

            onClick = onDismiss

        ) {

            Icon(

                imageVector = Icons.Default.Close,

                contentDescription = null

            )

        }

    }

}


@Composable
private fun AttendanceSection(
    attendance: AttendanceHistoryUiModel
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {

            Column(
                modifier = Modifier.padding(20.dp)
            ) {

                Text(
                    text = "Attendance",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(20.dp))

                DetailItem(
                    label = "Date",
                    value = "${attendance.date} (${attendance.day})"
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                DetailItem(
                    label = "Working Hours",
                    value = attendance.totalHours
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                DetailItem(
                    label = "Status",
                    value = attendance.status.name.replace("_", " ")
                )

                Spacer(modifier = Modifier.height(16.dp))

                PunchCard(
                    title = "Punch In",
                    time = attendance.punchIn,
                    location = attendance.punchInLocation,
                    isPunchIn = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                PunchCard(
                    title = "Punch Out",
                    time = attendance.punchOut,
                    location = attendance.punchOutLocation,
                    isPunchIn = false
                )

            }

        }

    }

}

@Composable
private fun DetailItem(
    label: String,
    value: String
) {

    Column {

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            Modifier.height(4.dp)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold
        )

    }

}


@Composable
private fun PunchCard(
    title: String,
    time: String,
    location: String,
    isPunchIn: Boolean
) {

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = if (isPunchIn)
                        Icons.Outlined.Login
                    else
                        Icons.Outlined.Logout,
                    contentDescription = null,
                    tint = if (isPunchIn)
                        Color(0xFF16A34A)
                    else
                        Color(0xFFDC2626)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

            }

            Spacer(modifier = Modifier.height(20.dp))

            DetailItem(
                label = "Time",
                value = time
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp)
            )

            DetailItem(
                label = "Location",
                value = location
            )

        }

    }

}