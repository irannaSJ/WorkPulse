package com.example.workpulse.feature.faceRecognition.data

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class FaceImageAnalyzer @Inject constructor(
    private val faceDetector: FaceDetector
) : ImageAnalysis.Analyzer {

    private val scope =
        CoroutineScope(
            SupervisorJob() + Dispatchers.Default
        )

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {

        val mediaImage = imageProxy.image

        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        val inputImage = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        scope.launch {

            try {

                val faces =
                    faceDetector.detectFaces(inputImage)

                Log.d(
                    TAG,
                    "Detected faces = ${faces.size}"
                )

                faces.forEachIndexed { index, face ->

                    Log.d(
                        TAG,
                        "Face[$index] bounds = ${face.boundingBox}"
                    )
                }

            } catch (exception: Exception) {

                Log.e(
                    TAG,
                    "Face detection failed",
                    exception
                )

            } finally {

                imageProxy.close()
            }
        }
    }

    fun close() {
        scope.cancel()
    }

    companion object {
        private const val TAG = "FaceImageAnalyzer"
    }
}