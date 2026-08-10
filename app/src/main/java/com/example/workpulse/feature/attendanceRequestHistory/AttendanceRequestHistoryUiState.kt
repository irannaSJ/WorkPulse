package com.example.workpulse.feature.attendanceRequestHistory

import com.example.workpulse.data.local.entity.AttendanceRequestEntity
import com.example.workpulse.data.local.entity.AttendanceRequestStatus
import com.example.workpulse.feature.attendanceRequest.AttendanceRequestType

data class AttendanceRequestHistoryUiState(

    val isLoading: Boolean = true,

    val attendanceRequests: List<AttendanceRequestHistoryCardUi> =
        emptyList(),

    val searchQuery: String = "",

    val selectedFilter: AttendanceRequestHistoryFilter =
        AttendanceRequestHistoryFilter.ALL,

    val selectedRequest: AttendanceRequestHistoryCardUi? = null,

    val showDetailsBottomSheet: Boolean = false,

    val errorMessage: String? = null

)

data class AttendanceRequestHistoryCardUi(

    val id: Long,

    val erpNextId: String?,

    val requestType: AttendanceRequestType,

    val fromDate: String,

    val toDate: String,

    val includeHolidays: Boolean,

    val explanation: String,

    val status: AttendanceRequestStatus,

    val appliedOn: String

)


enum class AttendanceRequestHistoryFilter {

    ALL,

    PENDING,

    APPROVED,

    REJECTED,

    CANCELLED

}