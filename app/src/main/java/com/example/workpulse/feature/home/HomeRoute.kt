package com.example.workpulse.feature.home.presentation

import androidx.compose.remote.creation.profile.Profile
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.workpulse.feature.home.HomeViewModel

import android.Manifest
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.example.workpulse.feature.home.domain.model.AttendanceResult


@Composable
fun HomeRoute(
    onNavigateToProfile : () -> Unit,
    onViewAllClick : () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->

            if (granted) {

                viewModel.clearAttendanceResult()

                viewModel.onAttendanceClick()

            } else {

                Toast.makeText(
                    context,
                    "Location permission is required to mark attendance.",
                    Toast.LENGTH_LONG
                ).show()

                viewModel.clearAttendanceResult()
            }

        }


    LaunchedEffect(uiState.attendanceResult) {

        when (uiState.attendanceResult) {

            AttendanceResult.PermissionRequired -> {

                locationPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION
                )

            }

            AttendanceResult.GpsDisabled -> {

                Toast.makeText(
                    context,
                    "Please enable Location Services.",
                    Toast.LENGTH_LONG
                ).show()

                context.startActivity(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                )

                viewModel.clearAttendanceResult()
            }

            AttendanceResult.LocationUnavailable -> {

                Toast.makeText(
                    context,
                    "Unable to get current location.",
                    Toast.LENGTH_LONG
                ).show()

                viewModel.clearAttendanceResult()
            }

            AttendanceResult.AlreadyPunchedIn -> {

                Toast.makeText(
                    context,
                    "Already punched in today.",
                    Toast.LENGTH_SHORT
                ).show()

                viewModel.clearAttendanceResult()
            }

            AttendanceResult.Success -> {

                viewModel.clearAttendanceResult()
            }

            is AttendanceResult.Error -> {

                Toast.makeText(
                    context,
                    (uiState.attendanceResult as AttendanceResult.Error).message,
                    Toast.LENGTH_LONG
                ).show()

                viewModel.clearAttendanceResult()
            }

            null -> Unit
        }

    }

    HomeScreen(
        uiState = uiState,
        onAttendanceClick = viewModel :: onAttendanceClick,
        onNavigateToProfile = onNavigateToProfile,
        onViewAllClick = onViewAllClick
    )


}


