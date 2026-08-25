package com.example.workpulse.feature.login.presentation

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow

import androidx.lifecycle.ViewModel

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.example.workpulse.data.repository.AuthRepository
import com.example.workpulse.data.repository.FaceEmbeddingRepository
import com.example.workpulse.core.datastore.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val faceEmbeddingRepository: FaceEmbeddingRepository,
    private val sessionManager: SessionManager
)  : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState : StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun UpdateEmail(email : String){
        _uiState.value = _uiState.value.copy(
            email = email
        )
    }

    fun UpdatePassword(password : String){
        _uiState.value = _uiState.value.copy(
            password = password
        )
    }

    fun TooglePasswordVisibility(){
        _uiState.value = _uiState.value.copy(
            isPasswordVisible = !_uiState.value.isPasswordVisible
        )
    }


    fun login(){
        val currentState = _uiState.value


        if (currentState.email.isBlank() || currentState.password.isBlank()){
            _uiState.value = currentState.copy(
               errorMessage = "Please enter a email and password"
            )
            return
        }

        viewModelScope.launch{
            _uiState.value = currentState.copy(
                isLoading = true,
                errorMessage = null
            )
            Log.d(TAG, "Starting login")
            val result = authRepository.login(
                username = uiState.value.email,
                password = uiState.value.password
            ).onSuccess {

                val employeeId = sessionManager.getEmployeeId()
                val requiresRegistration = employeeId.isBlank() || !faceEmbeddingRepository.hasEmbedding(employeeId)
                Log.i(TAG, "Face enrollment check completed: registered=${!requiresRegistration}")


                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoginSuccessful = true,
                        requiresFaceRegistration = requiresRegistration
                    )
                }
            }.onFailure { exception ->

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Login failed"
                    )
                }
                Log.d(TAG, "Login failed")
            }
            Log.d(TAG, "Login completed: success=${result.isSuccess}")

        }


    }

    fun onLoginNavigationComplete() {
        _uiState.update {
            it.copy(isLoginSuccessful = false)
        }
    }

    private companion object { const val TAG = "LoginViewModel" }

}
