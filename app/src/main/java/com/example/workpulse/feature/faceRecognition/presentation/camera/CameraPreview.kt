package com.example.workpulse.feature.faceRecognition.presentation.camera

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.workpulse.feature.faceRecognition.data.face.FaceDetector
import com.example.workpulse.feature.faceRecognition.data.face.FaceEmbeddingModel
import com.example.workpulse.feature.faceRecognition.data.face.FaceImageAnalyzer
import com.google.mlkit.vision.face.Face

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner,
    faceEmbeddingModel: FaceEmbeddingModel,
    onFacesDetected: (List<Face>) -> Unit
) {

    val context = androidx.compose.ui.platform.LocalContext.current

    val previewView = remember {
        PreviewView(context)
    }
    val faceDetector = remember {
        FaceDetector()
    }
    val faceAnalyzer = remember(onFacesDetected) {
        FaceImageAnalyzer(
            faceDetector = faceDetector,
            faceEmbeddingModel = faceEmbeddingModel,
            onFacesDetected = onFacesDetected
        )
    }

    DisposableEffect(lifecycleOwner) {

        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(context)

        val executor = ContextCompat.getMainExecutor(context)

        cameraProviderFuture.addListener({

            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(
                    ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                )
                .build()
                .also {
                    it.setAnalyzer(
                        executor,
                        faceAnalyzer
                    )
                }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )

            } catch (exception: Exception) {

                exception.printStackTrace()
            }

        }, executor)

        onDispose {
            faceDetector.close()
            cameraProviderFuture.get().unbindAll()
        }
    }

    AndroidView(
        factory = {
            previewView
        },
        modifier = modifier
    )
}