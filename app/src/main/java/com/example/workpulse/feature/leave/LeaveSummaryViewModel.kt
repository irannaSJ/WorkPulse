package com.example.workpulse.feature.leave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workpulse.data.repository.EmployeeRepository
import com.example.workpulse.data.repository.LeaveRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaveSummaryViewModel @Inject constructor(
    private val leaveRepository: LeaveRepository,
    private val employeeRepository: EmployeeRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(LeaveSummaryUiState())

    val uiState: StateFlow<LeaveSummaryUiState> = _uiState.asStateFlow()

    init {
        observeLeaveBalance()
        syncLeaveBalance()
        observeEmployee()
    }

    private fun observeLeaveBalance(){

        viewModelScope.launch {

            leaveRepository.observeLeaveBalance().collect { leaveBalance ->

                _uiState.update {

                    it.copy(
                        leaveBalance = leaveBalance,
//                        employeeId = leaveBalance?.employeeId ?: "",
//                        employeeName = leaveBalance?.employeeName ?: "",
//                        isLoading = false
                        isLoading = false
                    )
                }
            }
        }
    }


    private fun observeEmployee() {

        viewModelScope.launch {

            employeeRepository.getEmployee().collect { employee ->

                _uiState.update {

                    it.copy(
                        employeeName = employee?.employeeName ?: "",
                        employeeId = employee?.employeeId ?: "",
                        designation = employee?.designation.orEmpty(),
                        employeeImage = employee?.profileImage.orEmpty()
                    )
                }
            }
        }
    }

    private fun syncLeaveBalance(){

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {

                leaveRepository.syncLeaveBalance()

            } catch (exception: Exception) {

                _uiState.update {

                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message
                    )
                }
            }
        }
    }
}