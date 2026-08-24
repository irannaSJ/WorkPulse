package com.example.workpulse.feature.faceRecognition.presentation

import android.Manifest
import android.content.Context
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.workpulse.feature.faceRecognition.model.FaceRecognitionMode
import com.example.workpulse.feature.faceRecognition.model.FaceRecognitionState
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun FaceRecognitionScreen(
    mode: FaceRecognitionMode,
    viewModel: FaceRecognitionViewModel,
    onRegistrationComplete: () -> Unit = {},
    onVerified: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val state by viewModel.state.collectAsState()
    var permitted by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { permitted = it }
    val executor: ExecutorService = remember { Executors.newSingleThreadExecutor() }
    val previewView = remember { PreviewView(context) }

    LaunchedEffect(Unit) { if (!permitted) permissionLauncher.launch(Manifest.permission.CAMERA) }
    LaunchedEffect(permitted, mode) { if (permitted) viewModel.start(mode) }
    LaunchedEffect(state) {
        when (state) {
            FaceRecognitionState.RegistrationComplete -> onRegistrationComplete()
            FaceRecognitionState.Verified -> onVerified()
            else -> Unit
        }
    }
    DisposableEffect(Unit) { onDispose { executor.shutdown(); viewModel.stopCamera() } }

    Box(Modifier.fillMaxSize()) {
        if (permitted) {
            AndroidView(modifier = Modifier.fillMaxSize(), factory = { previewView.apply { scaleType = PreviewView.ScaleType.FILL_CENTER } })
            LaunchedEffect(previewView, permitted) {
                bindCamera(context, lifecycleOwner, previewView, executor, viewModel)
            }
            Overlay(state, onRetry = viewModel::retry, onBack = onBack)
        } else {
            Column(Modifier.fillMaxSize().padding(24.dp), Arrangement.Center, Alignment.CenterHorizontally) {
                Text("Camera permission is required for face verification.")
                Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) { Text("Allow camera") }
                Button(onClick = onBack) { Text("Back") }
            }
        }
    }
}

private fun bindCamera(context: Context, lifecycleOwner: androidx.lifecycle.LifecycleOwner, previewView: PreviewView, executor: ExecutorService, viewModel: FaceRecognitionViewModel) {
    ProcessCameraProvider.getInstance(context).addListener({
        try {
            val provider = ProcessCameraProvider.getInstance(context).get()
            val preview = Preview.Builder().build().also { it.surfaceProvider = previewView.surfaceProvider }
            val analysis = ImageAnalysis.Builder().setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build()
            analysis.setAnalyzer(executor, viewModel::processCameraFrame)
            provider.unbindAll()
            provider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_FRONT_CAMERA, preview, analysis)
        } catch (e: Exception) {
            Log.e(TAG, "Camera unavailable", e)
            viewModel.cameraUnavailable()
        }
    }, ContextCompat.getMainExecutor(context))
}

@Composable
private fun Overlay(state: FaceRecognitionState, onRetry: () -> Unit, onBack: () -> Unit) {
    Column(Modifier.fillMaxWidth().background(Color.Black.copy(alpha = .60f)).padding(20.dp)) {
        Text(messageFor(state), color = Color.White)
        if (state is FaceRecognitionState.Rejected || state is FaceRecognitionState.Error) {
            Button(onClick = onRetry) { Text("Try again") }
            Button(onClick = onBack) { Text("Back") }
        }
    }
}

private fun messageFor(state: FaceRecognitionState): String = when (state) {
    FaceRecognitionState.Idle, FaceRecognitionState.Initializing -> "Preparing face recognition…"
    is FaceRecognitionState.Guidance -> state.message
    is FaceRecognitionState.CapturingReference -> "Capturing face ${state.capturedSamples}/${state.requiredSamples}"
    is FaceRecognitionState.Comparing -> "Comparing face…"
    FaceRecognitionState.RegistrationComplete -> "Face registration complete"
    FaceRecognitionState.Verified -> "Face verified"
    is FaceRecognitionState.Rejected -> state.message
    is FaceRecognitionState.Error -> state.message
}

private const val TAG = "FaceRecognitionScreen"
