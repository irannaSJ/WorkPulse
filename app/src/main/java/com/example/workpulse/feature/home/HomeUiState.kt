package com.example.workpulse.feature.home.presentation

import com.example.workpulse.feature.home.domain.model.AttendanceResult

data class HomeUiState(

    val attendanceState: AttendanceState = AttendanceState.NOT_PUNCHED_IN,

    val workingSeconds: Long = 0L,

    val punchInTime: Long? = null,

    val punchOutTime: Long? = null,

    // Employee Information (from Room)
    val employeeId: String = "",

    val employeeName: String = "",

    val designation: String = "",

    val department: String = "",

    val company: String = "",

    val profileImage: String = "",


    val isLoading: Boolean = false,

    val errorMessage: String? = null,

    val shouldRequestLocationPermission: Boolean = false,

    val shouldOpenLocationSettings: Boolean = false,

    val locationError: String? = null,

    val attendanceResult: AttendanceResult? = null,

    val remainingLeaves: Int = 0,
    val isLogoutSuccessful: Boolean = false,
    val requiresFaceRegistration: Boolean = false,
)
