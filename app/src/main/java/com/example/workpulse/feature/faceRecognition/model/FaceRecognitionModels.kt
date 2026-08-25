package com.example.workpulse.feature.faceRecognition.model

sealed interface FaceRecognitionMode {
    data object Registration : FaceRecognitionMode
    data object Verification : FaceRecognitionMode
}

sealed interface FaceRecognitionState {
    data object Idle : FaceRecognitionState
    data object Initializing : FaceRecognitionState
    data class Guidance(val message: String) : FaceRecognitionState
    data class CapturingReference(val capturedSamples: Int, val requiredSamples: Int) : FaceRecognitionState
    data class Comparing(val similarity: Float) : FaceRecognitionState
    data object RegistrationComplete : FaceRecognitionState
    data object Verified : FaceRecognitionState
    data class Rejected(val message: String) : FaceRecognitionState
    data class Error(val message: String) : FaceRecognitionState
}
