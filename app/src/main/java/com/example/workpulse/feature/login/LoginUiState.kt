package com.example.workpulse.feature.login

data class LoginUiState(
    val email : String = "",
    val password : String = "",
    val isLoading : Boolean =false,
    val isPasswordVisible : Boolean = false,
    val isLoginSuccessful : Boolean = false,
    val errorMessage : String? = null,
//    val isLoadingSuccessfull : Boolean = false
)