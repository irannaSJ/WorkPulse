package com.example.workpulse.feature.attendanceRequestHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workpulse.data.local.entity.AttendanceRequestEntity
import com.example.workpulse.data.local.entity.AttendanceRequestStatus
import com.example.workpulse.data.repository.AttendanceRequestRepository
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
class AttendanceRequestHistoryViewModel @Inject constructor(
    private val repository: AttendanceRequestRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            AttendanceRequestHistoryUiState()
        )

    val uiState: StateFlow<AttendanceRequestHistoryUiState> =
        _uiState.asStateFlow()

    private var allRequests =
        emptyList<AttendanceRequestHistoryCardUi>()

    init {
        observeAttendanceRequests()
    }



    /**
     * Observe requests from Room.
     *
     * The UI never communicates directly with ERPNext.
     * Room remains the source of truth.
     */
    private fun observeAttendanceRequests() {

        viewModelScope.launch {

            repository
                .observeAttendanceRequests()
                .collect { requests ->

                    val cards = requests.map {
                        it.toHistoryCardUi()
                    }

                    allRequests = cards

                    _uiState.update {

                        it.copy(

                            attendanceRequests = cards,

                            isLoading = false,

                            errorMessage = null

                        )

                    }

                    applyFilters()
                }
        }
    }

    /**
     * Search attendance requests.
     */
    fun onSearchQueryChanged(
        query: String
    ) {

        _uiState.update {

            it.copy(
                searchQuery = query
            )

        }

        applyFilters()
    }

    /**
     * Filter attendance requests by status.
     */
    fun onFilterChanged(
        filter: AttendanceRequestHistoryFilter
    ) {

        _uiState.update {

            it.copy(
                selectedFilter = filter
            )

        }

        applyFilters()
    }

    /**
     * Apply search + status filter.
     */
    private fun applyFilters() {

        val state = _uiState.value

        val query =
            state.searchQuery
                .trim()
                .lowercase()

        val selectedFilter =
            state.selectedFilter

        val filteredRequests =
            allRequests.filter { request ->

                val matchesSearch =

                    query.isBlank() ||

                            request.requestType.displayName
                                .lowercase()
                                .contains(query) ||

                            request.explanation
                                .lowercase()
                                .contains(query)

                val matchesFilter =

                    when (selectedFilter) {

                        AttendanceRequestHistoryFilter.ALL ->
                            true

                        AttendanceRequestHistoryFilter.PENDING ->
                            request.status ==
                                    AttendanceRequestStatus.PENDING

                        AttendanceRequestHistoryFilter.APPROVED ->
                            request.status ==
                                    AttendanceRequestStatus.APPROVED

                        AttendanceRequestHistoryFilter.REJECTED ->
                            request.status ==
                                    AttendanceRequestStatus.REJECTED

                        AttendanceRequestHistoryFilter.CANCELLED ->
                            request.status ==
                                    AttendanceRequestStatus.CANCELLED
                    }

                matchesSearch && matchesFilter
            }

        _uiState.update {

            it.copy(
                attendanceRequests = filteredRequests
            )

        }
    }

    /**
     * Open request details.
     */
    fun onRequestClicked(
        request: AttendanceRequestHistoryCardUi
    ) {

        _uiState.update {

            it.copy(

                selectedRequest = request,

                showDetailsBottomSheet = true

            )

        }
    }

    /**
     * Close request details.
     */
    fun hideDetailsBottomSheet() {

        _uiState.update {

            it.copy(

                selectedRequest = null,

                showDetailsBottomSheet = false

            )

        }
    }
}

/**
 * Convert Room Entity → UI model.
 */
private fun AttendanceRequestEntity.toHistoryCardUi():

        AttendanceRequestHistoryCardUi {

    return AttendanceRequestHistoryCardUi(

        id = id,

        erpNextId = erpNextId,

        requestType = requestType,

        fromDate = formatDate(fromDate),

        toDate = formatDate(toDate),

        includeHolidays = includeHolidays,

        explanation = reason,

        status = requestStatus,

        appliedOn = formatDate(createdAt)

    )
}

/**
 * Format Room timestamp for display.
 */
private fun formatDate(
    time: Long?
): String {

    if (time == null) {
        return "-"
    }

    return SimpleDateFormat(
        "dd MMM yyyy",
        Locale.getDefault()
    ).format(
        Date(time)
    )
}