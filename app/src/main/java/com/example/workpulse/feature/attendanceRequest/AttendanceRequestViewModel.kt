package com.example.workpulse.feature.attendanceRequest

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workpulse.data.repository.AttendanceRequestRepository
import com.example.workpulse.data.repository.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AttendanceRequestViewModel @Inject constructor(
    private val attendanceRequestRepository: AttendanceRequestRepository,
    private val employeeRepository : EmployeeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AttendanceRequestUiState())
    val uiState: StateFlow<AttendanceRequestUiState> = _uiState.asStateFlow()

    init {
        observeEmployee()
        observeAttendanceRequests()
    }


    private fun observeEmployee(){
        viewModelScope.launch {
            employeeRepository
                .getEmployee()
                .collect { employee ->
                    _uiState.update {
                        it.copy(
                            employeeName = employee?.employeeName?: ""
                        )
                    }
                }
        }
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


    fun submitAttendanceRequest() {
        val request = _uiState.value
        Log.d("ATTENDANCE REQUEST SUBMISSION", "Submit attendance request  reached")

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSubmitting = true,
                    errorMessage = null,
                    successMessage = null
                )
            }


            Log.d(
                "AttendanceRequest",
                """
    Employee : ${uiState.value.employeeName}
    From Date : ${uiState.value.fromDate}
    To Date : ${uiState.value.toDate}
    Request Type : ${uiState.value.requestType}
    Explanation : ${uiState.value.explanation}
    """.trimIndent()
            )

            attendanceRequestRepository.createAttendanceRequest(
                attendanceDate = formatAttendanceDate(request.fromDate),
                reason = request.requestType,
                fromDate = request.fromDate,
                toDate = request.toDate,
                location = "Pune",
                sourceAttendanceId = request.sourceAttendanceId
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        isSubmitting = false,
                        successMessage = "Attendance request saved successfully."
                    )
                }
                resetForm()
            }.onFailure {
                exception ->
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

                fromDate = null,

                toDate = null,

                requestType = null,

                explanation = "",

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



    fun onExplanationChanged(
        explanation: String
    ) {

        _uiState.update {

            it.copy(

                explanation = explanation,

                errorMessage = null

            )

        }

    }



    fun onRequestTypeSelected(

        requestType: AttendanceRequestType

    ) {

        _uiState.update {

            it.copy(

                requestType = requestType,

                errorMessage = null

            )

        }

    }




    fun showFromDatePicker() {

        _uiState.update {

            it.copy(
                showFromDatePicker = true
            )

        }

    }

    fun hideFromDatePicker() {

        _uiState.update {

            it.copy(
                showFromDatePicker = false
            )

        }

    }

    fun showToDatePicker() {

        _uiState.update {

            it.copy(
                showToDatePicker = true
            )

        }

    }

    fun hideToDatePicker() {

        _uiState.update {

            it.copy(
                showToDatePicker = false
            )

        }

    }

    fun showRequestTypeDialog() {

        _uiState.update {

            it.copy(
                showRequestTypeDialog = true
            )

        }

    }

    fun hideRequestTypeDialog() {

        _uiState.update {

            it.copy(
                showRequestTypeDialog = false
            )

        }

    }

    fun onFromDateSelected(
        date: Long
    ) {

        _uiState.update {

            it.copy(

                fromDate = date,

                errorMessage = null

            )

        }

    }

    fun onToDateSelected(
        date: Long
    ) {

        _uiState.update {

            it.copy(

                toDate = date,

                errorMessage = null

            )

        }

    }
}



private fun formatAttendanceDate(
    time: Long?
): String {

    if (time == null) return ""

    return java.text.SimpleDateFormat(
        "yyyy-MM-dd",
        java.util.Locale.getDefault()
    ).format(java.util.Date(time))
}
