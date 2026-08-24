package com.example.workpulse.feature.splash.presentation

data class SplashUiState(
    val isLoading : Boolean = true,
    val loadingMessage: String = "Preparing workspace...",
    val destination: SplashDestination? = null
)

enum class SplashDestination{
    LOGIN,
    HOME,
    FACE_REGISTRATION
}
