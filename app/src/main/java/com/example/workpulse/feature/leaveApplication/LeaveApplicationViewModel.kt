package com.example.workpulse.feature.leaveApplication

import android.util.Log
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




//    fun onLeaveTypeSelected(leaveType: LeaveType){
//        _uiState.update {
//            currentState -> currentState.copy(
//                leaveType = leaveType,
//                errorMessage = null,
//                showSuggestionDialog = false,
//                suggestedLeaveTypes = emptyList()
//
//            )
//        }
//    }

    fun onLeaveTypeSelected(
        leaveType: LeaveType
    ) {

        viewModelScope.launch {

            val balance = leaveApplicationRepository
                .getAvailableBalance(leaveType)

            _uiState.update {

                it.copy(

                    leaveType = leaveType,

                    availableBalance = balance,

                    errorMessage = null,

                    showSuggestionDialog = false,

                    suggestedLeaveTypes = emptyList()

                )

            }

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
        _uiState.update { it.copy(
            reason = reason.take(500),
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

        Log.d("Leave APplication Save button Clicked", "save button clicked")
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
                            isSubmitting = false,
                            successMessage = "Leave Application saved successfully"
                        )
                    }

//                    onResetClicked()
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


    fun clearSuccessMessage() {

        _uiState.update {

            it.copy(
                successMessage = null
            )

        }

    }


    fun clearErrorMessage() {

        _uiState.update {

            it.copy(
                errorMessage = null
            )

        }

    }

    fun hideSuggestionDialog() {

        _uiState.update {

            it.copy(
                showSuggestionDialog = false
            )

        }

    }
}
