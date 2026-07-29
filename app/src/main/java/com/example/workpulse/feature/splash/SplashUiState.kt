package com.example.workpulse.feature.splash

data class SplashUiState(
    val isLoading : Boolean = true,
    val destination: SplashDestination? = null
)

enum class SplashDestination{
    LOGIN,
    HOME
}