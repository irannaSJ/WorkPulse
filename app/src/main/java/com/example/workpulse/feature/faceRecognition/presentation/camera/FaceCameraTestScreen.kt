package com.example.workpulse.feature.faceRecognition.presentation.camera

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workpulse.feature.faceRecognition.data.face.FaceEmbeddingModel
import com.example.workpulse.feature.faceRecognition.data.face.FaceModelInspector
import com.example.workpulse.feature.faceRecognition.presentation.FaceDetectionViewModel

@Composable
fun FaceCameraTestScreen(
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val viewModel: FaceDetectionViewModel = viewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val faceEmbeddingModel = remember {
        FaceEmbeddingModel(context)
    }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->

            hasCameraPermission = granted
        }



    LaunchedEffect(Unit) {

        try {

            faceEmbeddingModel.initialize()

        } catch (exception: Exception) {

            exception.printStackTrace()
        }
    }
    DisposableEffect(Unit) {

        onDispose {
            faceEmbeddingModel.close()
        }
    }



    LaunchedEffect(Unit) {

        if (!hasCameraPermission) {
            permissionLauncher.launch(
                Manifest.permission.CAMERA
            )
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        if (hasCameraPermission) {

            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                lifecycleOwner = lifecycleOwner,
                faceEmbeddingModel = faceEmbeddingModel,
                onFacesDetected = { faces ->
                    viewModel.updateFaceCount(faces.size)
                }
            )
            Text(
                text = when {
                    state.faceCount == 0 ->
                        "No face detected"

                    state.faceCount == 1 ->
                        "Face detected"

                    else ->
                        "${state.faceCount} faces detected"
                },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 32.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface.copy(
                            alpha = 0.85f
                        ),
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(
                        horizontal = 20.dp,
                        vertical = 10.dp
                    ),
                color = MaterialTheme.colorScheme.onSurface
            )


        } else {

            Text(
                text = "Camera permission is required"
            )
        }
    }
}