package com.example.workpulse.feature.leaveHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workpulse.data.local.entity.EmployeeEntity
import com.example.workpulse.data.local.entity.LeaveApplicationEntity
import com.example.workpulse.data.local.entity.LeaveApplicationStatus
import com.example.workpulse.data.repository.EmployeeRepository
import com.example.workpulse.data.repository.LeaveHistoryRepository
import com.example.workpulse.feature.leaveHistory.components.LeaveFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LeaveHistoryViewModel @Inject constructor(
    private val repository: LeaveHistoryRepository,
    private val employeeRepository: EmployeeRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            LeaveHistoryUiState()
        )

    private var allLeaveApplications =
        emptyList<LeaveHistoryCardUi>()

    val uiState: StateFlow<LeaveHistoryUiState> =
        _uiState.asStateFlow()

    private var employee: EmployeeEntity? = null

    init {
        observeLeaveApplications()
        observeEmployee()
    }


    private fun observeEmployee() {

        viewModelScope.launch {

            employeeRepository
                .getEmployee()
                .collect { employeeEntity ->

                    employee = employeeEntity

                }

        }

    }
    private fun observeLeaveApplications() {

        viewModelScope.launch {

            repository
                .getLeaveApplications()
                .collect { applications ->

                    val cards = applications.map {

                        it.toLeaveHistoryCardUi()

                    }

                    allLeaveApplications = cards

                    val summary = LeaveSummaryUi(

                        total = applications.size,

                        approved = applications.count {
                            it.applicationStatus == LeaveApplicationStatus.APPROVED
                        },

                        pending = applications.count {
                            it.applicationStatus == LeaveApplicationStatus.PENDING
                        },

                        rejected = applications.count {
                            it.applicationStatus == LeaveApplicationStatus.REJECTED
                        },

                        cancelled = applications.count {
                            it.applicationStatus == LeaveApplicationStatus.CANCELLED
                        }

                    )

                    _uiState.update {

                        it.copy(

                            leaveApplications = allLeaveApplications,

                            summary = summary,

                            isLoading = false

                        )

                    }

                }

        }

    }



    fun onSearchQueryChanged(query: String) {

        _uiState.update {

            it.copy(
                searchQuery = query
            )

        }

        applyFilters()

    }


    private fun applyFilters() {

        val query = _uiState.value.searchQuery
            .trim()
            .lowercase()

        val selectedFilter = _uiState.value.selectedFilter

        val filteredList = allLeaveApplications.filter { leave ->

            val matchesSearch =

                query.isBlank() ||

                        leave.leaveType.lowercase().contains(query) ||

                        leave.reason.lowercase().contains(query)

            val matchesFilter = when (selectedFilter) {

                LeaveFilter.ALL ->
                    true

                LeaveFilter.APPROVED ->
                    leave.status == LeaveApplicationStatus.APPROVED

                LeaveFilter.PENDING ->
                    leave.status == LeaveApplicationStatus.PENDING

                LeaveFilter.REJECTED ->
                    leave.status == LeaveApplicationStatus.REJECTED

                LeaveFilter.CANCELLED ->
                    leave.status == LeaveApplicationStatus.CANCELLED

            }

            matchesSearch && matchesFilter

        }

        _uiState.update {

            it.copy(

                leaveApplications = filteredList

            )

        }

    }

    fun onFilterChanged(filter: LeaveFilter) {

        _uiState.update {

            it.copy(
                selectedFilter = filter
            )

        }

        applyFilters()

    }


    fun onLeaveClicked(
        leave: LeaveHistoryCardUi
    ) {

        val employee = employee ?: return

        _uiState.update {

            it.copy(

                leaveDetails = LeaveDetailsUi(

                    leaveType = leave.leaveType,

                    status = leave.status,

                    employeeName = employee.employeeName,

                    company = employee.company,

                    fromDate = leave.fromDate,

                    toDate = leave.toDate,

                    requestedDays = leave.duration,

                    reason = leave.reason,

                    appliedOn = leave.appliedOn,

                    erpNextId = leave.erpNextId

                ),

                showBottomSheet = true

            )

        }

    }

    fun hideBottomSheet() {

        _uiState.update {

            it.copy(

                leaveDetails = null,

                showBottomSheet = false

            )

        }

    }

}

private fun LeaveApplicationEntity.toLeaveHistoryCardUi(): LeaveHistoryCardUi {

    return LeaveHistoryCardUi(

        leaveType = leaveType,

        reason = description,

        fromDate = formatDate(fromDate),

        toDate = formatDate(toDate),

        duration = "$requestedDays Day${if (requestedDays > 1) "s" else ""}",

        appliedOn = formatDate(createdAt),

        status = applicationStatus,
        erpNextId = erpNextId,

    )

}

private fun formatDate(time: Long): String {

    val formatter = SimpleDateFormat(
        "dd MMM yyyy",
        Locale.getDefault()
    )

    return formatter.format(Date(time))

}
