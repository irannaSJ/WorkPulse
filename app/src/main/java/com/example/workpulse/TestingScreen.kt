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
import com.example.workpulse.feature.config.domain.ConfigurationManager
import com.example.workpulse.feature.config.domain.WorkPulseConfig
import com.example.workpulse.feature.config.mapper.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkPulseConfigTestViewModel @Inject constructor(
    private val configurationManager : ConfigurationManager
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var configuration by mutableStateOf<WorkPulseConfig?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadCachedConfiguration() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                configurationManager.loadFromCache()

                configuration = configurationManager.configuration.value

                Log.d("WorkPulseConfig","Loaded through ConfigurationManager : $configuration")
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

    fun syncConfiguration() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

             try {
//                 val result  = repository.syncConfiguration()
//                 configuration = result
//                 Log.d("WorkPulseConfig","Synced Configuration: $result")

                 configurationManager.sync()
                 configuration = configurationManager.configuration.value
                 Log.d(
                     "WorkPulseConfig",
                     "Synced through ConfigurationManager: $configuration"
                 )
             }catch (e: Exception) {
                 errorMessage = e.message ?: "Configuration sync failed"

                 Log.e(
                     "WorkPulseConfig",
                     "Configuration sync failed",
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

//        Button(
//            onClick = {
//                viewModel.fetchConfiguration()
//            },
//            enabled = !viewModel.isLoading
//        ) {
//            Text("Fetch Configuration")
//        }

        Button(
            onClick = {
                viewModel.loadCachedConfiguration()
            },
            enabled = !viewModel.isLoading
        ) {
            Text("Load Cached Configuration")
        }

        Button(onClick = {viewModel.syncConfiguration()}, enabled = !viewModel.isLoading) {
            Text("Sync Configuration")
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