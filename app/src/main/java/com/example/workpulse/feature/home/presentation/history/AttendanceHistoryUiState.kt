package com.example.workpulse.feature.home.presentation.history

import java.time.Month

data class AttendanceHistoryUiState(

    val attendanceList: List<AttendanceHistoryUiModel> = emptyList(),

    val isLoading: Boolean = false,

    val errorMessage: String? = null,
    val currentMonth: String = "",

    val presentDays: Int = 0,

    val totalWorkingHours: String = "0h",

    val averageWorkingHours: String = "0h 0m",

)


data class MonthlySummary(

    val month: String,

    val presentDays: Int,

    val totalHours: String,

    val averageHours: String

)