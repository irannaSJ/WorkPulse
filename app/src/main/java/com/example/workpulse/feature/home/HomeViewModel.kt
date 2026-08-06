package com.example.workpulse.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workpulse.core.worker.SyncScheduler
import com.example.workpulse.data.repository.AttendanceRepository
import com.example.workpulse.data.repository.AuthRepository
import com.example.workpulse.data.repository.EmployeeRepository
import com.example.workpulse.data.repository.LeaveRepository
import com.example.workpulse.feature.attendance.data.local.entity.AttendanceStatus
import com.example.workpulse.feature.home.presentation.AttendanceState
import com.example.workpulse.feature.home.presentation.HomeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val attendanceRepository: AttendanceRepository,
    private val leaveRepository : LeaveRepository,
    private val authRepository: AuthRepository,
    private val syncScheduler : SyncScheduler
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null


    init {
        observeEmployee()
        observeTodayAttendance()
        observeLeaveBalance()
        syncScheduler.scheduleLeaveSync()

    }



    private fun observeLeaveBalance() {

        viewModelScope.launch {

            leaveRepository.observeLeaveBalance().collect { balance ->

                val remainingLeaves =
                    (balance?.casualLeave ?: 0.0) +
                            (balance?.sickLeave ?: 0.0) +
                            (balance?.privilegeLeave ?: 0.0) +
                            (balance?.compensatoryOff ?: 0.0) +
                            (balance?.leaveWithoutPay ?: 0.0)

                _uiState.update {

                    it.copy(
                        remainingLeaves = remainingLeaves.toInt()
                    )
                }
            }
        }
    }

    private fun observeEmployee(){
        viewModelScope.launch{
            employeeRepository.getEmployee().collectLatest { employee ->
                if (employee != null) {
                    _uiState.value = _uiState.value.copy(
                        employeeId = employee.employeeId,
                        employeeName = employee.employeeName,
                        designation = employee.designation ?: "",
                        department = employee.department?: "",
                        company = employee.company?: "",
                        profileImage = employee.profileImage ?: ""

                    )
                }
            }
        }
    }





    private fun observeTodayAttendance(){
        stopWorkingTimer()
        viewModelScope.launch {
            attendanceRepository.observeTodayAttendance().collectLatest { attendance ->
                if(attendance== null){
                    _uiState.value = _uiState.value.copy(
                        attendanceState = AttendanceState.NOT_PUNCHED_IN,
                        workingSeconds = 0,
                        punchInTime = null,
                        punchOutTime = null
                    )
                    return@collectLatest
                }

                _uiState.value = _uiState.value.copy(
                    attendanceState = when (attendance.status) {

                        AttendanceStatus.NOT_PUNCHED_IN ->{
                            stopWorkingTimer()
                            AttendanceState.NOT_PUNCHED_IN
                        }

                        AttendanceStatus.PUNCHED_IN -> {
                            attendance.punchInTime?.let {
                                startWorkingTimer(it)
                            }
                            AttendanceState.PUNCHED_IN
                        }

                        AttendanceStatus.PUNCHED_OUT -> {
                            stopWorkingTimer()
                            AttendanceState.PUNCHED_OUT
                        }
                    },

                    workingSeconds =
                        if (attendance.status == AttendanceStatus.PUNCHED_OUT)
                            attendance.workingSeconds
                        else
                            _uiState.value.workingSeconds,

                    punchInTime = attendance.punchInTime,

                    punchOutTime = attendance.punchOutTime
                )
            }
        }
    }

    private fun startWorkingTimer(punchInTime: Long) {

        timerJob?.cancel()

        timerJob = viewModelScope.launch {

            while (isActive) {

                val workingSeconds =
                    ((System.currentTimeMillis() - punchInTime) / 1000)

                _uiState.value = _uiState.value.copy(
                    workingSeconds = workingSeconds
                )

                delay(1000)
            }
        }
    }

    private fun stopWorkingTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun onAttendanceClick(){
        viewModelScope.launch {
            when(uiState.value.attendanceState){
                AttendanceState.NOT_PUNCHED_IN -> {
                    val result =  attendanceRepository.punchIn()

                    _uiState.value = _uiState.value.copy(
                        attendanceResult = result
                    )
                }

                AttendanceState.PUNCHED_IN -> {
                    val result = attendanceRepository.punchOut()

                    _uiState.value = _uiState.value.copy(
                        attendanceResult = result
                    )
                }

                AttendanceState.PUNCHED_OUT ->{

                }

                else -> {}
            }
        }
    }

    fun clearAttendanceResult() {

        _uiState.value = _uiState.value.copy(
            attendanceResult = null
        )

    }


    fun logout(){
        viewModelScope.launch {
            try {
                authRepository.logout()

                _uiState.value = _uiState.value.copy(
                    isLogoutSuccessful = true
                )
            }catch (e: Exception){
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message
                )
            }
        }
    }
}