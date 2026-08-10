package com.example.workpulse.feature.attendanceRequest

import com.example.workpulse.data.local.entity.AttendanceRequestEntity

data class AttendanceRequestUiState(
    val employeeName : String = "",
    val attendanceDate: String = "",
    val fromDate : Long? = null,
    val toDate : Long? = null,
    val location : String = "",
    val requestedPunchInTime: Long? = null,
    val requestedPunchOutTime: Long? = null,
    val requestType : AttendanceRequestType? = null,
    val explanation : String = "",
    val sourceAttendanceId: Long? = null,
    val attendanceRequests: List<AttendanceRequestEntity> = emptyList(),
    val isLoading: Boolean = true,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null,
    val showFromDatePicker: Boolean = false,
    val includeHolidays: Boolean = false,

    val showToDatePicker: Boolean = false,

    val showRequestTypeDialog: Boolean = false
)

enum class AttendanceRequestType(
    val displayName : String
){
    ON_DUTY("On Duty"),
    WORK_FROM_HOME("Work From Home")
}
