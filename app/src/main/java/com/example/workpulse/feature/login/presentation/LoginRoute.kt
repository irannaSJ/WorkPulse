package com.example.workpulse.feature.login.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginRoute(
    onNavigateToHome : () -> Unit,
    onNavigateToFaceRegistration: () -> Unit,
    viewModel : LoginViewModel= hiltViewModel()
){
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.value.isLoginSuccessful) {
        if(uiState.value.isLoginSuccessful){
            if (uiState.value.requiresFaceRegistration) onNavigateToFaceRegistration() else onNavigateToHome()
            viewModel.onLoginNavigationComplete()
        }
    }

    LoginScreen(
        uiState = uiState.value,
        onEmailChanged = viewModel :: UpdateEmail,
        onPasswordChanged = viewModel :: UpdatePassword,
        onLoginClick = viewModel :: login
    )


}
