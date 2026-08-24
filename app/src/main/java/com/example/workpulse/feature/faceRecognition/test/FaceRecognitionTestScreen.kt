package com.example.workpulse.feature.faceRecognition.test

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.workpulse.feature.faceRecognition.data.FaceRecognitionEngine
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.sqrt

@Composable
fun FaceRecognitionTestScreen() {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    /*
     * TEST ONLY
     *
     * We create a separate engine instance so this screen
     * does not modify or depend on the production ViewModel.
     */
    val engine = remember {
        FaceRecognitionEngine(context)
    }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var isCapturingReference by remember {
        mutableStateOf(false)
    }

    var referenceReady by remember {
        mutableStateOf(false)
    }

    var referenceCount by remember {
        mutableStateOf(0)
    }

    var similarity by remember {
        mutableStateOf<Float?>(null)
    }

    var faceDetected by remember {
        mutableStateOf(false)
    }

    val referenceSamples =
        remember {
            mutableStateListOf<FloatArray>()
        }

    var referenceEmbedding by remember {
        mutableStateOf<FloatArray?>(null)
    }

    /*
     * Prevent multiple frames from entering the
     * recognition pipeline simultaneously.
     */
    val processingFrame =
        remember {
            AtomicBoolean(false)
        }

    val cameraExecutor =
        remember {
            Executors.newSingleThreadExecutor()
        }

    val previewView =
        remember {
            PreviewView(context)
        }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            hasCameraPermission = granted
        }

    /*
     * Initialize MobileFaceNet.
     */
    LaunchedEffect(Unit) {

        try {

            engine.initialize()

            Log.d(
                TAG,
                "Test engine initialized"
            )

        } catch (exception: Exception) {

            Log.e(
                TAG,
                "Engine initialization failed",
                exception
            )
        }
    }

    /*
     * Ask for camera permission.
     */
    LaunchedEffect(Unit) {

        if (!hasCameraPermission) {

            permissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        }
    }

    /*
     * Start CameraX.
     */
    LaunchedEffect(
        hasCameraPermission
    ) {

        if (!hasCameraPermission) {
            return@LaunchedEffect
        }

        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(
                context
            )

        cameraProviderFuture.addListener({

            try {

                val cameraProvider =
                    cameraProviderFuture.get()

                val preview =
                    Preview.Builder()
                        .build()
                        .also {
                            it.surfaceProvider =
                                previewView.surfaceProvider
                        }

                val imageAnalysis =
                    ImageAnalysis.Builder()
                        .setBackpressureStrategy(
                            ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                        )
                        .build()

                imageAnalysis.setAnalyzer(
                    cameraExecutor
                ) { imageProxy ->

                    /*
                     * Drop this frame if another frame
                     * is currently being processed.
                     */
                    if (
                        !processingFrame.compareAndSet(
                            false,
                            true
                        )
                    ) {

                        imageProxy.close()

                        return@setAnalyzer
                    }

                    scope.launch {

                        try {

                            val faceBitmap =
                                engine.extractFaceFromFrame(
                                    imageProxy
                                )

                            faceDetected =
                                faceBitmap != null

                            if (faceBitmap == null) {
                                return@launch
                            }

                            try {

                                val embedding =
                                    engine.getEmbedding(
                                        faceBitmap
                                    )

                                /*
                                 * ------------------------------------------------
                                 * REFERENCE CAPTURE
                                 * ------------------------------------------------
                                 */
                                if (
                                    isCapturingReference &&
                                    !referenceReady
                                ) {

                                    referenceSamples.add(
                                        embedding
                                    )

                                    referenceCount =
                                        referenceSamples.size

                                    Log.d(
                                        TAG,
                                        "Reference sample " +
                                                "$referenceCount / 5 captured"
                                    )

                                    if (
                                        referenceSamples.size >=
                                        REQUIRED_REFERENCE_SAMPLES
                                    ) {

                                        val averaged =
                                            engine.averageAndNormalize(
                                                referenceSamples.toList()
                                            )

                                        referenceEmbedding =
                                            averaged

                                        referenceReady =
                                            true

                                        isCapturingReference =
                                            false

                                        Log.d(
                                            TAG,
                                            "Reference embedding ready"
                                        )

                                        Log.d(
                                            TAG,
                                            "Reference size = " +
                                                    "${averaged.size}"
                                        )

                                        Log.d(
                                            TAG,
                                            "Reference magnitude = " +
                                                    calculateMagnitude(
                                                        averaged
                                                    )
                                        )
                                    }
                                }

                                /*
                                 * ------------------------------------------------
                                 * LIVE VERIFICATION
                                 * ------------------------------------------------
                                 */
                                val reference =
                                    referenceEmbedding

                                if (
                                    reference != null &&
                                    !isCapturingReference
                                ) {

                                    val currentSimilarity =
                                        engine.cosineSimilarity(
                                            reference,
                                            embedding
                                        )

                                    similarity =
                                        currentSimilarity

                                    Log.d(
                                        TAG,
                                        "Similarity with reference = " +
                                                currentSimilarity
                                    )
                                }

                            } finally {

                                faceBitmap.recycle()
                            }

                        } catch (exception: Exception) {

                            Log.e(
                                TAG,
                                "Frame processing failed",
                                exception
                            )

                        } finally {

                            processingFrame.set(
                                false
                            )
                        }
                    }
                }

                val cameraSelector =
                    CameraSelector.DEFAULT_FRONT_CAMERA

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )

                Log.d(
                    TAG,
                    "Test camera started"
                )

            } catch (exception: Exception) {

                Log.e(
                    TAG,
                    "Camera start failed",
                    exception
                )
            }

        }, ContextCompat.getMainExecutor(context))
    }

    /*
     * Cleanup.
     */
    DisposableEffect(Unit) {

        onDispose {

            cameraExecutor.shutdown()

            engine.close()
        }
    }

    /*
     * UI
     */
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        if (hasCameraPermission) {

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    previewView
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.Black.copy(
                            alpha = 0.65f
                        )
                    )
                    .padding(16.dp),
                horizontalAlignment =
                    Alignment.CenterHorizontally
            ) {

                Text(
                    text =
                        if (faceDetected)
                            "Face detected"
                        else
                            "Looking for face...",
                    color = Color.White
                )

                Text(
                    modifier =
                        Modifier.padding(
                            top = 8.dp
                        ),
                    text =
                        when {

                            isCapturingReference ->
                                "Capturing reference: " +
                                        "$referenceCount / " +
                                        "$REQUIRED_REFERENCE_SAMPLES"

                            referenceReady ->
                                "Reference: READY"

                            else ->
                                "Reference: NOT CAPTURED"
                        },
                    color = Color.White
                )

                Text(
                    modifier =
                        Modifier.padding(
                            top = 8.dp
                        ),
                    text =
                        similarity?.let {
                            "Similarity: %.4f"
                                .format(it)
                        }
                            ?: "Similarity: --",
                    color = Color.White
                )

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                top = 12.dp
                            ),
                    horizontalArrangement =
                        Arrangement.SpaceEvenly
                ) {

                    Button(
                        enabled =
                            !isCapturingReference,
                        onClick = {

                            referenceSamples.clear()

                            referenceEmbedding =
                                null

                            referenceCount =
                                0

                            similarity =
                                null

                            referenceReady =
                                false

                            isCapturingReference =
                                true

                            Log.d(
                                TAG,
                                "Starting reference capture"
                            )
                        }
                    ) {

                        Text(
                            text =
                                "Capture Reference"
                        )
                    }

                    Button(
                        enabled =
                            referenceReady ||
                                    referenceSamples.isNotEmpty(),
                        onClick = {

                            referenceSamples.clear()

                            referenceEmbedding =
                                null

                            referenceCount =
                                0

                            referenceReady =
                                false

                            isCapturingReference =
                                false

                            similarity =
                                null

                            Log.d(
                                TAG,
                                "Reference cleared"
                            )
                        }
                    ) {

                        Text(
                            text =
                                "Clear"
                        )
                    }
                }
            }

        } else {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment =
                    Alignment.CenterHorizontally,
                verticalArrangement =
                    Arrangement.Center
            ) {

                Text(
                    text =
                        "Camera permission required"
                )

                Button(
                    modifier =
                        Modifier.padding(
                            top = 16.dp
                        ),
                    onClick = {
                        permissionLauncher.launch(
                            Manifest.permission.CAMERA
                        )
                    }
                ) {

                    Text(
                        text = "Allow Camera"
                    )
                }
            }
        }
    }
}

private fun calculateMagnitude(
    embedding: FloatArray
): Double {

    var sum = 0.0

    for (value in embedding) {

        sum +=
            value.toDouble() *
                    value.toDouble()
    }

    return sqrt(sum)
}

private const val REQUIRED_REFERENCE_SAMPLES = 5

private const val TAG =
    "FaceRecognitionTest"