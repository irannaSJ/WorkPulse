package com.example.workpulse.feature.faceRecognition.data

import com.google.mlkit.vision.face.Face
import kotlin.math.abs

/** Small, stateful guards shared by face registration and verification. */
class FaceQualityValidator {
    private var stableFrames = 0

    fun validate(face: Face, frameWidth: Int, frameHeight: Int): String? {
        val box = face.boundingBox
        val widthRatio = box.width().toFloat() / frameWidth
        val heightRatio = box.height().toFloat() / frameHeight
        val centerX = (box.centerX().toFloat() / frameWidth) - .5f
        val centerY = (box.centerY().toFloat() / frameHeight) - .5f
        val insideFrame = box.left >= frameWidth * .05f && box.right <= frameWidth * .95f &&
            box.top >= frameHeight * .05f && box.bottom <= frameHeight * .95f

        val reason = when {
            widthRatio < .28f || heightRatio < .28f -> "Face too far. Move closer."
            !insideFrame -> "Keep your whole face inside the frame."
            abs(centerX) > .18f || abs(centerY) > .18f -> "Center your face in the guide."
            abs(face.headEulerAngleX) > 15f || abs(face.headEulerAngleY) > 15f ||
                abs(face.headEulerAngleZ) > 15f -> "Look straight at the camera."
            face.leftEyeOpenProbability < 0f || face.rightEyeOpenProbability < 0f ->
                "Eyes are not clearly visible. Improve lighting."
            else -> null
        }
        stableFrames = if (reason == null) stableFrames + 1 else 0
        return reason
    }

    fun isStable(): Boolean = stableFrames >= REQUIRED_STABLE_FRAMES

    fun reset() { stableFrames = 0 }

    private companion object { const val REQUIRED_STABLE_FRAMES = 3 }
}

class BlinkLiveness {
    private enum class Step { WAITING_FOR_OPEN, WAITING_FOR_CLOSED, WAITING_FOR_REOPEN }
    private var step = Step.WAITING_FOR_OPEN
    private var startedAt = 0L

    fun check(face: Face, now: Long = System.currentTimeMillis()): Result {
        if (startedAt == 0L) startedAt = now
        if (now - startedAt > TIMEOUT_MS) return Result.Timeout
        val left = face.leftEyeOpenProbability
        val right = face.rightEyeOpenProbability
        if (left < 0f || right < 0f) return Result.Ambiguous
        val open = left >= OPEN_THRESHOLD && right >= OPEN_THRESHOLD
        val closed = left <= CLOSED_THRESHOLD && right <= CLOSED_THRESHOLD
        step = when (step) {
            Step.WAITING_FOR_OPEN -> if (open) Step.WAITING_FOR_CLOSED else step
            Step.WAITING_FOR_CLOSED -> if (closed) Step.WAITING_FOR_REOPEN else step
            Step.WAITING_FOR_REOPEN -> if (open) return Result.Passed else step
        }
        return if (step == Step.WAITING_FOR_CLOSED) Result.BlinkRequired else Result.ReopenRequired
    }

    fun reset() { step = Step.WAITING_FOR_OPEN; startedAt = 0L }

    sealed interface Result {
        data object Passed : Result
        data object BlinkRequired : Result
        data object ReopenRequired : Result
        data object Timeout : Result
        data object Ambiguous : Result
    }

    private companion object {
        const val OPEN_THRESHOLD = .70f
        const val CLOSED_THRESHOLD = .30f
        const val TIMEOUT_MS = 12_000L
    }
}
