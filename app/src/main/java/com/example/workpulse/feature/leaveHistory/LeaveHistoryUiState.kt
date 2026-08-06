package com.example.workpulse.feature.leaveHistory

import com.example.workpulse.data.local.entity.LeaveApplicationStatus
import com.example.workpulse.feature.leaveHistory.components.LeaveFilter


data class LeaveHistoryUiState(

    val isLoading: Boolean = true,

    val summary: LeaveSummaryUi = LeaveSummaryUi(),

    val searchQuery: String = "",

    val selectedFilter: LeaveFilter = LeaveFilter.ALL,

    val leaveApplications: List<LeaveHistoryCardUi> = emptyList(),

    val errorMessage: String? = null,

    val selectedLeave: LeaveHistoryCardUi? = null,

    val showBottomSheet: Boolean = false,
    val leaveDetails: LeaveDetailsUi? = null

)

data class LeaveHistoryCardUi(

    val leaveType: String,

    val reason: String,

    val fromDate: String,

    val toDate: String,

    val duration: String,

    val appliedOn: String,

    val status: LeaveApplicationStatus,

    val erpNextId: String?

)


data class LeaveSummaryUi(

    val total: Int = 0,

    val approved: Int = 0,

    val pending: Int = 0,

    val rejected: Int = 0,

    val cancelled: Int = 0

)


data class LeaveDetailsUi(
    val leaveType: String,
    val status: LeaveApplicationStatus,
    val employeeName: String,
    val company: String?,
    val fromDate: String,
    val toDate: String,
    val requestedDays: String,
    val reason: String,
    val appliedOn: String,
    val erpNextId: String?
)
