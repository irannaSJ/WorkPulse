package com.example.workpulse

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workpulse.data.repository.WorkPulseConfigRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TestingFile @Inject constructor(
    private val repository: WorkPulseConfigRepository
): ViewModel() {

    fun test(){
        viewModelScope.launch {
            try {
                val config = repository.fetchConfiguration()

                Log.d(
                    "WorkPulseConfig",
                    "Configuration: $config"
                )
            }catch (e: Exception) {
                Log.e(
                    "WorkPulseConfig",
                    "Failed to fetch configuration",
                    e
                )
            }
        }
    }
}