package com.example.workpulse.feature.compOffApplicationsHistory

import com.example.workpulse.data.local.entity.ApplicationStatus

data class CompOffApplicationHistoryUiState (
    val isLoading : Boolean = true,
    val compOffApplication : List<CompOffApplicationHistoryCardUi> = emptyList(),
    val selectedFilter : CompOffApplicationHistoryFilter = CompOffApplicationHistoryFilter.ALL,
    val selectedRequest : CompOffApplicationHistoryCardUi? = null,
    val showDetailsBottomSheet : Boolean = false,
    val errorMessage : String? = null,
    val compOffDetails: CompOffDetailsUi? = null,
    val showBottomSheet : Boolean = false
)

data class CompOffApplicationHistoryCardUi(
    val id: Long,
    val erpNextId: String?,
    val fromDate : String,
    val toDate : String,
    val reason : String,
    val status : ApplicationStatus,
    val appliedOn : String
)

enum class CompOffApplicationHistoryFilter{
    ALL,
    PENDING,
    APPROVED,
    REJECTED,
    CANCELLED
}

data class CompOffDetailsUi(
    val id: Long,
    val status: ApplicationStatus,
    val employeeName: String,
    val fromDate: String,
    val toDate: String,
//    val requestedDays: String,
    val reason: String,
    val appliedOn: String,
    val erpNextId: String?
)