package com.example.workpulse.feature.faceRecognition.data.face

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.mlkit.vision.face.Face
import android.util.Log
import com.example.workpulse.feature.faceRecognition.data.face.FaceImagePreprocessor
import com.example.workpulse.feature.faceRecognition.data.face.ImageProxyBitmapConverter

class FaceImageAnalyzer @Inject constructor(
    private val faceDetector: FaceDetector,
    private val faceEmbeddingModel: FaceEmbeddingModel,
    private val onFacesDetected: (List<Face>) -> Unit
) : ImageAnalysis.Analyzer {

    private val scope = CoroutineScope(Dispatchers.Default)

    private val bitmapConverter = ImageProxyBitmapConverter()
    private val faceImagePreprocessor = FaceImagePreprocessor()
    private val faceTensorConverter = FaceTensorConverter()
//    private var modelInitialized = false

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {

        val mediaImage = imageProxy.image

        if (mediaImage == null) {
            imageProxy.close()
            return
        }
//        if (!modelInitialized) {
//            faceEmbeddingModel.initialize()
//            modelInitialized = true
//        }

        val image = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        scope.launch {

            try {

                val faces = faceDetector.detectFaces(image)

                onFacesDetected(faces)
                if (faces.size == 1) {

                    val face = faces[0]

                    val bitmap = bitmapConverter.toBitmap(imageProxy)

                    if (bitmap != null) {

                        val faceBitmap =
                            faceImagePreprocessor.cropAndResize(
                                bitmap = bitmap,
                                boundingBox = face.boundingBox
                            )
                        val tensor =
                            faceTensorConverter.bitmapToFloatArray(
                                faceBitmap
                            )

                        val embedding =
                            faceEmbeddingModel.generateEmbedding(
                                tensor
                            )

                        Log.d(
                            "FaceEmbeddingTest",
                            "Embedding size = ${embedding.size}"
                        )

                        Log.d(
                            "FaceEmbeddingTest",
                            "First 10 values = ${
                                embedding
                                    .take(10)
                                    .joinToString()
                            }"
                        )

                        Log.d(
                            "FaceCropTest",
                            "Original bitmap = ${bitmap.width} x ${bitmap.height}"
                        )

                        Log.d(
                            "FaceCropTest",
                            "Face bounding box = ${face.boundingBox}"
                        )

                        Log.d(
                            "FaceCropTest",
                            "Cropped bitmap = ${faceBitmap.width} x ${faceBitmap.height}"
                        )
                    }
                }

                println(
                    "FaceRecognition: Detected ${faces.size} face(s)"
                )

            } catch (exception: Exception) {

                println(
                    "FaceRecognition: Detection failed: ${exception.message}"
                )

            } finally {

                imageProxy.close()
            }
        }
    }
}