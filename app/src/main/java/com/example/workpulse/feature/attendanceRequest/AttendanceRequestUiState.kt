package com.example.workpulse.feature.attendanceRequest

import com.example.workpulse.data.local.entity.AttendanceRequestEntity

data class AttendanceRequestUiState(
    val attendanceDate: String = "",
    val requestedPunchInTime: Long? = null,
    val requestedPunchOutTime: Long? = null,
    val reason: String = "",
    val sourceAttendanceId: Long? = null,
    val attendanceRequests: List<AttendanceRequestEntity> = emptyList(),
    val isLoading: Boolean = true,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
