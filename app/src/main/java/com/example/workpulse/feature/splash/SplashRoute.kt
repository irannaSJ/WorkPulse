package com.example.workpulse.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay

@Composable
fun SplashRoute(
    onNavigateToHome : () -> Unit,
    onNavigateToLogin : () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
//    LaunchedEffect(uiState.destination) {
//
//        if (uiState.destination != null) {
//
//            delay(3000)
//
//            when (uiState.destination) {
//
//                SplashDestination.LOGIN ->
//                    onNavigateToLogin()
//
//                SplashDestination.HOME ->
//                    onNavigateToHome()
//
//                else -> {}
//            }
//        }
//    }



//    SplashScreen(
//        uiState = uiState
//    )

    SplashScreen(
        uiState = uiState,
        onAnimationFinished = {

            when (uiState.destination) {

                SplashDestination.LOGIN ->
                    onNavigateToLogin()

                SplashDestination.HOME ->
                    onNavigateToHome()

                else -> {}
            }

        }
    )

}