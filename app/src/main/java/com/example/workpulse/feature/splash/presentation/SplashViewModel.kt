package com.example.workpulse.feature.splash.presentation


import androidx.lifecycle.ViewModel
import com.example.workpulse.core.datastore.SessionManager
import com.example.workpulse.data.repository.FaceEmbeddingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val sessionManager : SessionManager,
    private val faceEmbeddingRepository: FaceEmbeddingRepository
) : ViewModel()
{
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState : StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        initalize()
    }


    private fun initalize(){
        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    loadingMessage = "Preparing workspace..."
                )
            }
            delay(1000)

            _uiState.update {
                it.copy(
                    loadingMessage = "Checking Secure Session..."
                )
            }

            delay(1000)

            _uiState.update {
                it.copy(
                    loadingMessage = "Loading employee profile..."
                )
            }

            delay(1000)

            _uiState.update {
                it.copy(
                    loadingMessage = "Almost ready..."
                )
            }
            delay(700)

            val destination = if(sessionManager.isLoggedIn()){
                if (faceEmbeddingRepository.hasEmbedding(sessionManager.getEmployeeId())) SplashDestination.HOME
                else SplashDestination.FACE_REGISTRATION
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
