package com.example.workpulse.feature.leaveApplication

data class LeaveApplicationUiState(

    // Employee
    val employeeId: String = "",
    val employeeName: String = "",
    val company: String = "",

    // Leave
    val leaveType: LeaveType? = null,
    val availableBalance: Double? = null,

    // Dates
    val fromDate: Long? = null,
    val toDate: Long? = null ,
    val requestedDays: Int = 0,

    // Reason
    val reason: String = "",

    // UI
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,

    // Validation
    val errorMessage: String? = null,

    // Dialog
    val showSuggestionDialog: Boolean = false,
    val suggestedLeaveTypes: List<LeaveSuggestionUi> = emptyList()
)

data class LeaveSuggestionUi(

    val leaveType: LeaveType? = null,

    val availableDays: Double

)

enum class LeaveType(val displayName : String) {

    CASUAL("Casual Leave"),

    SICK("Sick Leave"),

    PRIVILEGE("Privilege Leave"),

    COMP_OFF("Compensatory Off"),

    LEAVE_WITHOUT_PAY("Leave Without Pay")

}