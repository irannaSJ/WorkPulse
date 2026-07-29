package com.example.workpulse.feature.leave

import com.example.workpulse.data.local.entity.LeaveBalanceEntity

data class LeaveSummaryUiState(

    val employeeName: String = "",

    val employeeId: String = "",

    val designation: String = "",

    val employeeImage: String = "",

    val totalLeaves: Int = 20,

    val usedLeaves: Int = 7,

    val remainingLeaves: Int = 13,

    val leaveTypes: List<LeaveTypeUiModel> = emptyList(),

    val isLoading: Boolean = false,

    val errorMessage: String? = null,
    val leaveBalance: LeaveBalanceEntity? = null,


)