package com.example.workpulse.feature.compOffApplication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workpulse.data.repository.CompOffApplicationRepository
import com.example.workpulse.data.repository.EmployeeRepository
import com.example.workpulse.feature.compOffApplicationsHistory.CompOffApplicationHistoryRoute
import com.example.workpulse.feature.leaveApplication.LeaveApplicationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompOffApplicationViewModel @Inject constructor(
    private val compOffApplicationRepository : CompOffApplicationRepository,
    private val employeeRepository: EmployeeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompOffApplicationUiState())
    val uiState : StateFlow<CompOffApplicationUiState> = _uiState.asStateFlow()

    init {
        observeEmployee()
    }

    private fun observeEmployee(){
        viewModelScope.launch {

            employeeRepository
                .getEmployee()
                .collect { employee ->
                    if(employee == null) return@collect

                    _uiState.update {
                        it.copy(
                            employeeId = employee.employeeId,
                            employeeName = employee.employeeName,
                            company = employee.company?: ""
                        )
                    }
                }
        }
    }

    fun onFromDateSelected(date : Long){
        _uiState.update {
            currentState -> currentState.copy(
                fromDate = date,
                errorMessage = null
            )
        }
    }

    fun onToDateSelected(date: Long){
        _uiState.update {
            currentState ->
            val days = if(currentState.fromDate != null){
                calculateRequestedDays(
                    fromDate = currentState.fromDate,
                    toDate = date
                )
            }else{
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

    fun onSaveClick(){

        if (uiState.value.isSubmitting ||
            uiState.value.isSubmitted) {
            return
        }
        Log.d(
            "CompOffSubmit",
            "Submit clicked: fromDate=${uiState.value.fromDate}, " +
                    "toDate=${uiState.value.toDate}, " +
                    "reason=${uiState.value.reason}"
        )

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isSubmitting = true,
                    errorMessage = null
                )
            }
            val result = compOffApplicationRepository.saveCompOffApplication(

                fromDate = uiState.value.fromDate,
                toDate = uiState.value.toDate,
                reason = uiState.value.reason,
                requestedDays = uiState.value.requestedDays
            )

            when (result){
                is LeaveApplicationResult.Success -> {
                    _uiState.update {
                        it.copy(
                            isSubmitting = false,
                            isSubmitted = true,
                            successMessage = "Comp Off Application Saved successfully"
                        )
                    }
                }
                is LeaveApplicationResult.ValidationFailed -> {
                    when(val validation =result.validationResult){
                        is LeaveApplicationResult.Error -> {
                            _uiState.update {
                                it.copy(
                                    isSubmitting = false,
                                    errorMessage = validation.message
                                )
                            }
                        }


                        else -> {}
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
        fromDate : Long,
        toDate : Long
    ): Int{
        return if (toDate < fromDate){
            0
        }else{
            (((toDate - fromDate) / (24*60*60*1000))+1).toInt()
        }
    }

    fun clearSuccessMessage(){
        _uiState.update {
            it.copy(
                successMessage = null
            )
        }
    }

    fun clearErrorMessage(){
        _uiState.update {
            it.copy(
                errorMessage = null
            )
        }
    }

    fun onResetClicked(){
        _uiState.update {
            it -> it.copy(
                fromDate = null,
                toDate = null,
                reason = "",
                requestedDays = 0,
            )
        }
    }
}


