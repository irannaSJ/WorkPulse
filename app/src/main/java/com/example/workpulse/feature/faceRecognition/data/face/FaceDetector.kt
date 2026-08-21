package com.example.workpulse.feature.faceRecognition.data.face

import android.graphics.PointF
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FaceDetector @Inject constructor() {

    private val detector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(
                FaceDetectorOptions.PERFORMANCE_MODE_FAST
            )
            .setLandmarkMode(
                FaceDetectorOptions.LANDMARK_MODE_NONE
            )
            .setClassificationMode(
                FaceDetectorOptions.CLASSIFICATION_MODE_NONE
            )
            .build()
    )

    suspend fun detectFaces(
        image: InputImage
    ): List<Face> {

        return detector
            .process(image)
            .await()
    }

    fun close() {
        detector.close()
    }
}