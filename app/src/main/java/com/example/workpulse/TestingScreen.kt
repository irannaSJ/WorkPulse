package com.example.workpulse

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workpulse.data.remote.dto.response.WorkPulseConfigDto
import com.example.workpulse.data.repository.WorkPulseConfigRepository
import com.example.workpulse.feature.config.domain.WorkPulseConfig
import com.example.workpulse.feature.config.mapper.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkPulseConfigTestViewModel @Inject constructor(
    private val repository: WorkPulseConfigRepository
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var configuration by mutableStateOf<WorkPulseConfig?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun fetchConfiguration() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val remoteConfig = repository.fetchConfiguration()
                repository.saveConfiguration(remoteConfig)

//                val result = remoteConfig.toDomain()
//                configuration = result
                val cachedConfig = repository.getCachedConfiguration()
                configuration = cachedConfig

//                Log.d(
//                    "WorkPulseConfig",
//                    "Configuration received: $result"
//                )
            } catch (e: Exception) {
                errorMessage = e.message ?: "Unknown error"

                Log.e(
                    "WorkPulseConfig",
                    "Failed to fetch configuration",
                    e
                )
            } finally {
                isLoading = false
            }
        }
    }

    fun loadCachedConfiguration() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val cachedConfig = repository.getCachedConfiguration()

                configuration = cachedConfig

                Log.d(
                    "WorkPulseConfig",
                    "Cached configuration: $cachedConfig"
                )
            } catch (e: Exception) {
                errorMessage = e.message ?: "Failed to load cached configuration"

                Log.e(
                    "WorkPulseConfig",
                    "Failed to load cached configuration",
                    e
                )
            } finally {
                isLoading = false
            }
        }
    }
}

@Composable
fun TestingScreen(
    viewModel: WorkPulseConfigTestViewModel = hiltViewModel()
) {
    val configuration = viewModel.configuration

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "WorkPulse Configuration Test"
        )

        Button(
            onClick = {
                viewModel.fetchConfiguration()
            },
            enabled = !viewModel.isLoading
        ) {
            Text("Fetch Configuration")
        }

        Button(
            onClick = {
                viewModel.loadCachedConfiguration()
            },
            enabled = !viewModel.isLoading
        ) {
            Text("Load Cached Configuration")
        }

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        }

        viewModel.errorMessage?.let { error ->
            Text(
                text = "Error: $error"
            )
        }

        configuration?.let { config ->

            Text(
                text = "Configuration: ${config.configurationName}"
            )

            Text(
                text = "Version: ${config.version}"
            )

            Text(
                text = "Theme Mode: ${config.theme.themeMode}"
            )

            Text(
                text = "Navigation Items: ${config.navigation.size}"
            )

            Text(
                text = "Quick Actions: ${config.quickActions.size}"
            )

            Text(
                text = "Home Sections: ${config.home.sections.size}"
            )

            Text(
                text = "Features: ${config.features.size}"
            )
        }
    }
}