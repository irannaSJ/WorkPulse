package com.example.workpulse.feature.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.workpulse.core.navigation.Screen

@Composable
fun ProfileRoute(
    onBackClick: () -> Unit,
    onLeaveClick : () -> Unit,
    onLogoutSuccess: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel(),
    onAttendanceHistoryClick : () -> Unit,

) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isLogoutSuccessful) {
        if (uiState.isLogoutSuccessful) {
            onLogoutSuccess()
        }
    }

    ProfileScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onLogout = {
            viewModel.logout()
        },
        onLeaveClick = onLeaveClick,
        onNotificationClick = {},

        onAttendanceHistoryClick = onAttendanceHistoryClick,
        viewModel = viewModel
    )
}