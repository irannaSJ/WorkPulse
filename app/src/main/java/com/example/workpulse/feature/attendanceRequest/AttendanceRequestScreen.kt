package com.example.workpulse.feature.attendanceRequest

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceRequestScreen(
    onBackClick: () -> Unit = {},
    viewModel: AttendanceRequestViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    var showFromDatePicker by rememberSaveable { mutableStateOf(false)}
    var showToDatePicker by rememberSaveable { mutableStateOf(false) }
    var showPunchInTimePicker by rememberSaveable { mutableStateOf(false) }
    var showPunchOutTimePicker by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearErrorMessage()
        }
    }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.resetForm()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Attendance Request") },
                navigationIcon = {
                    FilledTonalButton(
                        onClick = onBackClick,
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Request an attendance correction",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Choose the affected date, provide corrected times if needed, and explain the request.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    Row(modifier = Modifier.fillMaxWidth()) {
//                        RequestFieldButton(
//                            label = "Requested punch in",
//                            value = uiState.requestedPunchInTime.toDisplayTime(),
//                            icon = Icons.Outlined.Schedule,
//                            enabled = uiState.attendanceDate.isNotBlank(),
//                            modifier = Modifier.weight(1f),
//                            onClick = { showPunchInTimePicker = true }
//                        )
//                        Spacer(modifier = Modifier.width(12.dp))
//                        RequestFieldButton(
//                            label = "Requested punch out",
//                            value = uiState.requestedPunchOutTime.toDisplayTime(),
//                            icon = Icons.Outlined.Schedule,
//                            enabled = uiState.attendanceDate.isNotBlank(),
//                            modifier = Modifier.weight(1f),
//                            onClick = { showPunchOutTimePicker = true }
//                        )

                        RequestFieldButton(
                            label = "From date",
                            value = uiState.attendanceDate.toDisplayDate(),
                            icon = Icons.Outlined.CalendarMonth,
                            modifier = Modifier.weight(1f),
                            onClick = { showDatePicker = true },
                        )
                        Spacer(modifier = Modifier.width(12.dp))

                        RequestFieldButton(
                            label = "To date",
                            value = uiState.attendanceDate.toDisplayDate(),
                            icon = Icons.Outlined.CalendarMonth,
                            modifier = Modifier.weight(1f),
                            onClick = { showDatePicker = true },
                        )
                    }

                    OutlinedTextField(
                        value = uiState.reason,
                        onValueChange = viewModel::onReasonChanged,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Reason") },
                        placeholder = { Text("Explain why this correction is needed") },
                        supportingText = { Text("${uiState.reason.length}/500") },
                        minLines = 4,
                        maxLines = 6,
                        enabled = !uiState.isSubmitting
                    )
                }
            }

            Button(
                onClick = viewModel::submitAttendanceRequest,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isSubmitting
            ) {
                Text(if (uiState.isSubmitting) "Submitting…" else "Submit request")
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedDate ->
                            viewModel.onAttendanceDateSelected(selectedDate.toIsoDate())
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                FilledTonalButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showPunchInTimePicker) {
        AttendanceTimePickerDialog(
            initialTime = uiState.requestedPunchInTime,
            onDismiss = { showPunchInTimePicker = false },
            onTimeSelected = { hour, minute ->
                viewModel.onRequestedPunchInTimeSelected(
                    uiState.attendanceDate.toEpochMillis(hour, minute)
                )
                showPunchInTimePicker = false
            }
        )
    }

    if (showPunchOutTimePicker) {
        AttendanceTimePickerDialog(
            initialTime = uiState.requestedPunchOutTime,
            onDismiss = { showPunchOutTimePicker = false },
            onTimeSelected = { hour, minute ->
                viewModel.onRequestedPunchOutTimeSelected(
                    uiState.attendanceDate.toEpochMillis(hour, minute)
                )
                showPunchOutTimePicker = false
            }
        )
    }
}

@Composable
private fun RequestFieldButton(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))
        FilledTonalButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 14.dp)
        ) {
            Icon(icon, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(value)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AttendanceTimePickerDialog(
    initialTime: Long?,
    onDismiss: () -> Unit,
    onTimeSelected: (hour: Int, minute: Int) -> Unit
) {
    val initialLocalTime = initialTime?.let {
        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalTime()
    }
    val timePickerState = rememberTimePickerState(
        initialHour = initialLocalTime?.hour ?: 9,
        initialMinute = initialLocalTime?.minute ?: 0,
        is24Hour = false
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    onTimeSelected(timePickerState.hour, timePickerState.minute)
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            FilledTonalButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        TimePicker(state = timePickerState)
    }
}

private fun Long?.toDisplayTime(): String {
    if (this == null) return "Select time"

    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()))
}

private fun String.toDisplayDate(): String {
    if (isBlank()) return "Select date"

    return runCatching {
        LocalDate.parse(this).format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault()))
    }.getOrDefault(this)
}

private fun Long.toIsoDate(): String =
    Instant.ofEpochMilli(this)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()
        .toString()

private fun String.toEpochMillis(hour: Int, minute: Int): Long =
    LocalDateTime.of(LocalDate.parse(this), java.time.LocalTime.of(hour, minute))
        .atZone(ZoneId.systemDefault())
        .toInstant()
        .toEpochMilli()
