package com.example.workpulse.feature.faceRecognition.presentation

data class FaceDetectionState(
    val faceCount: Int = 0,
    val isFaceDetected: Boolean = false,
    val errorMessage: String? = null
)