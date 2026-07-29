package com.example.workpulse.feature.home.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workpulse.core.datastore.SessionManager
import com.example.workpulse.core.util.TimeUtils.formatDuration
import com.example.workpulse.core.util.TimeUtils.formatTime
import com.example.workpulse.data.repository.AttendanceRepository
import com.example.workpulse.feature.attendance.data.local.entity.AttendanceEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import com.example.workpulse.feature.home.presentation.history.AttendanceHistoryUiModel
import com.example.workpulse.feature.home.presentation.history.AttendanceStatus
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@HiltViewModel
class AttendanceHistoryViewModel @Inject constructor(
    private val attendanceRepository: AttendanceRepository,
    private val sessionManager : SessionManager
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(AttendanceHistoryUiState())

    val uiState = _uiState.asStateFlow()

    init {
        loadAttendanceHistory()
    }

    private fun loadAttendanceHistory() {

        viewModelScope.launch {

            sessionManager.employeeIdFlow.collect { employeeId ->

                if (employeeId.isNullOrBlank()) return@collect

                attendanceRepository
                    .getAttendanceHistory(employeeId)
                    .collect { attendance ->

                        val summary = calculateMonthlySummary(attendance)

                        _uiState.value = AttendanceHistoryUiState(

                            attendanceList = attendance.map {
                                it.toUiModel()
                            },

                            currentMonth = summary.month,

                            presentDays = summary.presentDays,

                            totalWorkingHours = summary.totalHours,

                            averageWorkingHours = summary.averageHours,

                            isLoading = false

                        )

                    }

            }

        }

    }

}




//mapper

fun AttendanceEntity.toUiModel(): AttendanceHistoryUiModel {

    return AttendanceHistoryUiModel(

        date = formatDate(attendanceDate),

        day = formatDay(attendanceDate),

        punchIn = formatTime(punchInTime),

        punchOut = formatTime(punchOutTime),

        punchInLocation = if (latitude != null && logitude != null)
            "$latitude, $logitude"
        else
            "--",

        punchOutLocation = if (latitude != null && logitude != null)
            "$latitude, $logitude"
        else
            "--",

        totalHours = formatWorkingHours(workingSeconds),

        status = AttendanceStatus.PRESENT
    )
}



private fun formatDate(date: String): String {

    return try {
        val localDate = LocalDate.parse(date)
        localDate.dayOfMonth.toString()
    } catch (e: Exception) {
        date
    }
}

private fun formatDay(date: String): String {

    return try {
        val localDate = LocalDate.parse(date)
        localDate.dayOfWeek.name
            .lowercase()
            .replaceFirstChar { it.uppercase() }
            .take(3)
    } catch (e: Exception) {
        ""
    }
}

private fun formatTime(time: Long?): String {

    if (time == null) return "--"

    val formatter = DateTimeFormatter.ofPattern(
        "hh:mm a",
        Locale.getDefault()
    )

    return Instant
        .ofEpochMilli(time)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(formatter)
}

private fun formatWorkingHours(seconds: Long): String {

    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60

    return "${hours}h ${minutes}m"
}



private fun calculateMonthlySummary(
    attendance: List<AttendanceEntity>
): MonthlySummary {

    if (attendance.isEmpty()) {

        return MonthlySummary(
            month = "",
            presentDays = 0,
            totalHours = "0h",
            averageHours = "0h 0m"
        )

    }

    val totalSeconds =
        attendance.sumOf { it.workingSeconds }

    val presentDays =
        attendance.count { it.punchInTime != null }

    val averageSeconds =
        if (presentDays == 0)
            0
        else
            totalSeconds / presentDays

    return MonthlySummary(

        month = getCurrentMonth(),

        presentDays = presentDays,

        totalHours = formatWorkingHours(totalSeconds),

        averageHours = formatWorkingHours(averageSeconds)

    )

}


private fun getCurrentMonth(): String {

    val formatter =
        DateTimeFormatter.ofPattern(
            "MMMM yyyy",
            Locale.getDefault()
        )

    return LocalDate.now().format(formatter)

}