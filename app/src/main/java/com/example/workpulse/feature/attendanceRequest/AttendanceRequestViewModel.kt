package com.example.workpulse.feature.attendanceRequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workpulse.data.repository.AttendanceRequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceRequestViewModel @Inject constructor(
    private val attendanceRequestRepository: AttendanceRequestRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AttendanceRequestUiState())
    val uiState: StateFlow<AttendanceRequestUiState> = _uiState.asStateFlow()

    init {
        observeAttendanceRequests()
    }

    private fun observeAttendanceRequests() {
        viewModelScope.launch {
            attendanceRequestRepository.observeAttendanceRequests().collect { requests ->
                _uiState.update {
                    it.copy(
                        attendanceRequests = requests,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onAttendanceDateSelected(attendanceDate: String) {
        _uiState.update {
            it.copy(
                attendanceDate = attendanceDate,
                errorMessage = null
            )
        }
    }

    fun onRequestedPunchInTimeSelected(time: Long?) {
        _uiState.update {
            it.copy(
                requestedPunchInTime = time,
                errorMessage = null
            )
        }
    }

    fun onRequestedPunchOutTimeSelected(time: Long?) {
        _uiState.update {
            it.copy(
                requestedPunchOutTime = time,
                errorMessage = null
            )
        }
    }

    fun onReasonChanged(reason: String) {
        _uiState.update {
            it.copy(
                reason = reason.take(500),
                errorMessage = null
            )
        }
    }

    /** Prefills the form when it was opened from an existing attendance record. */
    fun setSourceAttendance(
        sourceAttendanceId: Long,
        attendanceDate: String,
        punchInTime: Long?,
        punchOutTime: Long?
    ) {
        _uiState.update {
            it.copy(
                sourceAttendanceId = sourceAttendanceId,
                attendanceDate = attendanceDate,
                requestedPunchInTime = punchInTime,
                requestedPunchOutTime = punchOutTime,
                errorMessage = null
            )
        }
    }

    fun submitAttendanceRequest() {
        val request = _uiState.value

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSubmitting = true,
                    errorMessage = null,
                    successMessage = null
                )
            }

            attendanceRequestRepository.createAttendanceRequest(
                attendanceDate = request.attendanceDate,
                reason = request.reason,
                requestedPunchInTime = request.requestedPunchInTime,
                requestedPunchOutTime = request.requestedPunchOutTime,
                sourceAttendanceId = request.sourceAttendanceId
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        successMessage = "Attendance request saved successfully."
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        errorMessage = exception.message ?: "Failed to save attendance request."
                    )
                }
            }
        }
    }

    fun resetForm() {
        _uiState.update {
            it.copy(
                attendanceDate = "",
                requestedPunchInTime = null,
                requestedPunchOutTime = null,
                reason = "",
                sourceAttendanceId = null,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun clearSuccessMessage() {
        _uiState.update { it.copy(successMessage = null) }
    }

    fun clearErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
