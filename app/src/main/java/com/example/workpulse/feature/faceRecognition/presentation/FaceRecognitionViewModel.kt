package com.example.workpulse.feature.faceRecognition.presentation

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workpulse.core.datastore.SessionManager
import com.example.workpulse.data.repository.FaceEmbeddingRepository
import com.example.workpulse.feature.faceRecognition.data.BlinkLiveness
import com.example.workpulse.feature.faceRecognition.data.FaceQualityValidator
import com.example.workpulse.feature.faceRecognition.data.FaceRecognitionEngine
import com.example.workpulse.feature.faceRecognition.model.FaceRecognitionMode
import com.example.workpulse.feature.faceRecognition.model.FaceRecognitionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class FaceRecognitionViewModel @Inject constructor(
    private val engine: FaceRecognitionEngine,
    private val faceEmbeddingRepository: FaceEmbeddingRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _state = MutableStateFlow<FaceRecognitionState>(FaceRecognitionState.Idle)
    val state: StateFlow<FaceRecognitionState> = _state.asStateFlow()

    private val processing = AtomicBoolean(false)
    private val qualityValidator = FaceQualityValidator()
    private val blinkLiveness = BlinkLiveness()
    private val samples = mutableListOf<FloatArray>()
    private var mode: FaceRecognitionMode = FaceRecognitionMode.Registration
    private var employeeId = ""
    private var referenceEmbedding: FloatArray? = null
    private var completed = false

    fun start(recognitionMode: FaceRecognitionMode) {
        if (_state.value !is FaceRecognitionState.Idle) return
        mode = recognitionMode
        _state.value = FaceRecognitionState.Initializing
        viewModelScope.launch {
            try {
                employeeId = sessionManager.getEmployeeId()
                check(employeeId.isNotBlank()) { "Employee session is missing" }
                engine.initialize()
                if (mode is FaceRecognitionMode.Verification) {
                    referenceEmbedding = faceEmbeddingRepository.getEmbedding(employeeId)
                    check(referenceEmbedding?.size == EMBEDDING_SIZE) { "Registered face is missing" }
                }
                _state.value = FaceRecognitionState.Guidance("Center one face and blink once.")
            } catch (e: Exception) {
                Log.e(TAG, "Face recognition initialization failed", e)
                _state.value = FaceRecognitionState.Error(userMessage(e))
            }
        }
    }

    fun processCameraFrame(imageProxy: ImageProxy) {
        if (completed || !processing.compareAndSet(false, true)) {
            imageProxy.close()
            return
        }
        viewModelScope.launch {
            try {
                val frame = engine.analyzeFrame(imageProxy)
                var bitmapConsumed = false
                try {
                if (frame.faceCount != 1 || frame.face == null) {
                    resetGuards()
                    _state.value = FaceRecognitionState.Guidance(
                        if (frame.faceCount > 1) "Multiple faces detected. Only one person may be in frame."
                        else "No face detected. Center your face in the guide."
                    )
                    return@launch
                }
                val qualityReason = qualityValidator.validate(frame.face, frame.frameWidth, frame.frameHeight)
                if (qualityReason != null || !qualityValidator.isStable() || frame.bitmap == null) {
                    if (qualityReason != null) blinkLiveness.reset()
                    _state.value = FaceRecognitionState.Guidance(qualityReason ?: "Hold still for face quality check.")
                    return@launch
                }
                when (blinkLiveness.check(frame.face)) {
                    BlinkLiveness.Result.BlinkRequired -> {
                        _state.value = FaceRecognitionState.Guidance("Blink once to continue.")
                        return@launch
                    }
                    BlinkLiveness.Result.ReopenRequired -> {
                        _state.value = FaceRecognitionState.Guidance("Open your eyes to complete the blink.")
                        return@launch
                    }
                    BlinkLiveness.Result.Ambiguous -> {
                        _state.value = FaceRecognitionState.Guidance("Eyes are not clearly visible. Improve lighting.")
                        return@launch
                    }
                    BlinkLiveness.Result.Timeout -> {
                        resetGuards()
                        _state.value = FaceRecognitionState.Rejected("Blink timed out. Please try again.")
                        return@launch
                    }
                    BlinkLiveness.Result.Passed -> {
                        bitmapConsumed = true
                        processEmbedding(frame.bitmap)
                    }
                }
                } finally {
                    if (!bitmapConsumed) frame.bitmap?.recycle()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Face frame processing failed", e)
                _state.value = FaceRecognitionState.Error("Face verification failed. Please try again.")
            } finally {
                // analyzeFrame owns ImageProxy; only the cropped bitmap is owned here.
                processing.set(false)
            }
        }
    }

    private suspend fun processEmbedding(bitmap: Bitmap) {
        try {
            val embedding = engine.getEmbedding(bitmap)
            when (mode) {
                FaceRecognitionMode.Registration -> {
                    if (samples.size < REQUIRED_SAMPLES) samples += embedding
                    _state.value = FaceRecognitionState.CapturingReference(samples.size, REQUIRED_SAMPLES)
                    if (samples.size == REQUIRED_SAMPLES) {
                        faceEmbeddingRepository.saveEmbedding(employeeId, engine.averageAndNormalize(samples))
                        completed = true
                        _state.value = FaceRecognitionState.RegistrationComplete
                    }
                }
                FaceRecognitionMode.Verification -> {
                    if (samples.size < REQUIRED_VERIFICATION_SAMPLES) samples += embedding
                    _state.value = FaceRecognitionState.CapturingReference(
                        samples.size,
                        REQUIRED_VERIFICATION_SAMPLES
                    )
                    if (samples.size == REQUIRED_VERIFICATION_SAMPLES) {
                        val similarity = engine.cosineSimilarity(
                            referenceEmbedding!!,
                            engine.averageAndNormalize(samples)
                        )
                        val accepted = similarity >= RECOGNITION_THRESHOLD
                        Log.i(TAG, "Face comparison completed: accepted=$accepted")
                        _state.value = FaceRecognitionState.Comparing(similarity)
                        completed = true
                        _state.value = if (accepted) FaceRecognitionState.Verified
                        else FaceRecognitionState.Rejected("Face not recognized. Please try again.")
                    }
                }
            }
        } finally { bitmap.recycle() }
    }

    fun retry() {
        samples.clear(); completed = false; resetGuards()
        _state.value = FaceRecognitionState.Guidance("Center one face and blink once.")
    }

    fun cameraUnavailable() {
        _state.value = FaceRecognitionState.Error("Camera unavailable. Please try again.")
    }

    private fun resetGuards() { qualityValidator.reset(); blinkLiveness.reset() }
    fun stopCamera() = Unit
    // The engine is application-scoped and is intentionally reused between navigation destinations.
    override fun onCleared() { super.onCleared() }
    private fun userMessage(e: Exception) = if (e.message == "Registered face is missing") e.message!! else "Unable to start the camera. Please try again."
    private companion object {
        const val TAG = "FaceRecognitionViewModel"
        const val REQUIRED_SAMPLES = 5
        const val EMBEDDING_SIZE = 192
        /** Tune from collected genuine/impostor validation data before release. */
        const val RECOGNITION_THRESHOLD = .78f
        const val REQUIRED_VERIFICATION_SAMPLES = 3
    }
}
