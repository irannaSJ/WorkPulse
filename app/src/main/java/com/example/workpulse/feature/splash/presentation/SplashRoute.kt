package com.example.workpulse.feature.splash.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SplashRoute(
    onNavigateToHome : () -> Unit,
    onNavigateToLogin : () -> Unit,
    onNavigateToFaceRegistration: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    SplashScreen(
        uiState = uiState,
        onAnimationFinished = {

            when (uiState.destination) {

                SplashDestination.LOGIN ->
                    onNavigateToLogin()

                SplashDestination.HOME ->
                    onNavigateToHome()

                SplashDestination.FACE_REGISTRATION -> onNavigateToFaceRegistration()

                else -> {}
            }

        }
    )

}
