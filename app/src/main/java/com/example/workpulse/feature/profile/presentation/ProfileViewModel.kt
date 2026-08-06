package com.example.workpulse.feature.profile.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workpulse.core.datastore.SessionManager
import com.example.workpulse.data.repository.AuthRepository
import com.example.workpulse.data.repository.EmployeeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val employeeRepository: EmployeeRepository,
    private val authRepository: AuthRepository,
    private val sessionManager : SessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())

    val uiState : StateFlow<ProfileUiState> = _uiState.asStateFlow()



    init {
        observeEmployee()
        loadAppUuid()
    }

    private fun observeEmployee(){
        viewModelScope.launch {
            employeeRepository.getEmployee().collectLatest { employee ->
                if (employee == null) {

                    _uiState.value = _uiState.value.copy(
                        isLoading = false
                    )

                    return@collectLatest
                }
                _uiState.value = _uiState.value.copy(
                    employeeId = employee.employeeId,
                    employeeName = employee.employeeName,
                    employeeImage = employee.profileImage ?: "",
                    company = employee.company ?: "",
                    department = employee.department ?: "",
                    designation = employee.designation ?: "",
                    companyEmail = employee.companyEmail ?: "",
                    personalEmail = employee.personalEmail ?: "",
                    mobileNumber = employee.mobileNumber ?: "",
                    dateOfJoining = employee.dateOfJoining?: "",
//                    currentAddress = employee.currentAddress?: "",
                    currentAddress = "Baner, Pune",
                    isLoading = false,
                )

            }
        }
    }

    fun loadAppUuid(){
        viewModelScope.launch {
            val uuid = sessionManager.getOrCreateAppUuid()
            _uiState.update {
                it.copy(
                    app_uid =  uuid
                )
            }
        }
    }

    fun enableEditing(){
        _uiState.update {
            it.copy(
                isEditing = true
            )
        }
        Log.d("Profile","Edit button Enabled")
    }

    fun disableEditing(){
        _uiState.update {
            it.copy(
                isEditing = false
            )
        }
    }

    fun updateProfile(
        personalEmail: String,
        imageUri: String
    ) {

        viewModelScope.launch {

            employeeRepository.updateProfile(
                personalEmail,
                imageUri
            )

        }

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