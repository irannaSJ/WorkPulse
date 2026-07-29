package com.example.workpulse.feature.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.workpulse.feature.login.presentation.LoginScreen

@Composable
fun LoginRoute(
    onNavigateToHome : () -> Unit,
    viewModel : LoginViewModel= hiltViewModel()
){
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.value.isLoginSuccessful) {
        if(uiState.value.isLoginSuccessful){
            onNavigateToHome()
            viewModel.onLoginNavigationComplete()
        }
    }

    LoginScreen(
        uiState = uiState.value,
        onEmailChanged = viewModel :: UpdateEmail,
        onPasswordChanged = viewModel :: UpdatePassword,
        onPasswordVisibility = viewModel :: TooglePasswordVisibility,
        onLoginClick = viewModel :: login
    )


}