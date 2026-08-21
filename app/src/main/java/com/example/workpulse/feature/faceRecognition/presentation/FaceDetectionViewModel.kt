package com.example.workpulse.feature.faceRecognition.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FaceDetectionViewModel : ViewModel() {

    private val _state = MutableStateFlow(
        FaceDetectionState()
    )

    val state: StateFlow<FaceDetectionState> =
        _state.asStateFlow()

    fun updateFaceCount(count: Int) {

        _state.value = FaceDetectionState(
            faceCount = count,
            isFaceDetected = count > 0
        )
    }

    fun updateError(message: String) {

        _state.value = FaceDetectionState(
            errorMessage = message
        )
    }
}