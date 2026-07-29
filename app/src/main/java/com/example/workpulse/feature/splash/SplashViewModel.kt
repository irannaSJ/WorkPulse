package com.example.workpulse.feature.splash


import androidx.lifecycle.ViewModel
import com.example.workpulse.core.datastore.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionManager : SessionManager
) : ViewModel()
{
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState : StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        checkLoginState()
    }

    private fun checkLoginState(){
        viewModelScope.launch{

            val destination = if(sessionManager.isLoggedIn()){
                SplashDestination.HOME
            }else{
                SplashDestination.LOGIN
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                destination= destination
            )
        }
    }



}