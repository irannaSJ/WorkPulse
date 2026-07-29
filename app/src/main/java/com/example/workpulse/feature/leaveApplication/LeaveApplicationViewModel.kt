package com.example.workpulse.feature.leaveApplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workpulse.data.repository.LeaveApplicationRepository
import com.example.workpulse.data.repository.LeaveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton


@HiltViewModel
class LeaveApplicationViewModel @Inject constructor(
    private val leaveApplicationRepository: LeaveApplicationRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(LeaveApplicationUiState())
    val uiState : StateFlow<LeaveApplicationUiState> = _uiState.asStateFlow()

    fun onLeaveTypeSelected(leaveType: LeaveType){
        _uiState.update {
            currentState -> currentState.copy(
                leaveType = leaveType,
                errorMessage = null,
                showSuggestionDialog = false,
                suggestedLeaveTypes = emptyList()

            )
        }
    }

    fun onFromDateSelected(date : Long){
        _uiState.update {
            currentState -> currentState.copy(
                fromDate = date,
                requestedDays = 0,
                errorMessage = null
            )
        }
    }

    fun onToDateSelected(date: Long){
        _uiState.update {
            currentState ->
            val days = if (currentState.fromDate != null){
                calculateRequestedDays(
                    fromDate = currentState.fromDate,
                    toDate = date
                )}else{
                    0
                }

            currentState.copy(
                toDate = date,
                requestedDays = days,
                errorMessage = null
            )
        }
    }

    fun onReasonChanged(reason : String){

        if(reason.length <=500) return
        _uiState.update { currentState -> currentState.copy(
            reason = reason,
            errorMessage = null
        ) }

    }

    fun onResetClicked(){
        _uiState.update { currentState -> currentState.copy(
            leaveType = null,
            availableBalance = null,
            fromDate = null,
            toDate = null,
            requestedDays = 0,
            showSuggestionDialog = false,
            reason = "",
            suggestedLeaveTypes = emptyList()
        ) }
    }


    fun onSaveClick(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSubmitting = true,
                    errorMessage = null
                )
            }

            val result = leaveApplicationRepository.saveLeaveApplication(
                leaveType = uiState.value.leaveType,
                fromDate = uiState.value.fromDate,
                toDate = uiState.value.toDate,
                requestedDays = uiState.value.requestedDays,
                reason = uiState.value.reason
            )


            when (result) {

                is LeaveApplicationResult.Success -> {

                    _uiState.update {
                        it.copy(
                            isSubmitting = false
                        )
                    }

                    onResetClicked()
                }

                is LeaveApplicationResult.ValidationFailed -> {

                    when (val validation = result.validationResult) {

                        is LeaveValidationResult.Error -> {

                            _uiState.update {
                                it.copy(
                                    isSubmitting = false,
                                    errorMessage = validation.message
                                )
                            }

                        }

                        is LeaveValidationResult.InsufficientBalance -> {

                            _uiState.update {
                                it.copy(
                                    isSubmitting = false,
                                    availableBalance = validation.availableBalance,
                                    suggestedLeaveTypes = validation.suggestions,
                                    showSuggestionDialog = true
                                )
                            }

                        }

                        LeaveValidationResult.Success -> Unit
                    }
                }

                is LeaveApplicationResult.Error -> {

                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = result.message
                        )
                    }

                }

            }
        }
    }


    private fun calculateRequestedDays(
        fromDate: Long,
        toDate: Long
    ): Int {

        return if (toDate < fromDate) {
            0
        } else {
            (((toDate - fromDate) / (24 * 60 * 60 * 1000)) + 1).toInt()
        }

    }
}
