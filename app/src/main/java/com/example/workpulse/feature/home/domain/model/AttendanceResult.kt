package com.example.workpulse.feature.home.domain.model

sealed interface AttendanceResult {

    data object Success : AttendanceResult

    data object AlreadyPunchedIn : AttendanceResult

    data object PermissionRequired : AttendanceResult

    data object GpsDisabled : AttendanceResult

    data object LocationUnavailable : AttendanceResult

    data class Error(
        val message: String
    ) : AttendanceResult
}