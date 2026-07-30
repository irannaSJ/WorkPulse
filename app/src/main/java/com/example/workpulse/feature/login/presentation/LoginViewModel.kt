package com.example.workpulse.feature.login.presentation

import kotlinx.coroutines.flow.MutableStateFlow

import androidx.lifecycle.ViewModel

import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.example.workpulse.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
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
            authRepository.login(
                username = uiState.value.email,
                password = uiState.value.password
            ).onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoginSuccessful = true
                    )
                }
            }.onFailure { exception ->

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message ?: "Login failed"
                    )
                }
            }

        }


    }

    fun onLoginNavigationComplete() {
        _uiState.update {
            it.copy(isLoginSuccessful = false)
        }
    }

}