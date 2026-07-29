package com.example.workpulse.feature.home.presentation.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun AttendanceHistoryRoute(
    onBackClick: () -> Unit
) {

    val viewModel: AttendanceHistoryViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()

    AttendanceHistoryScreen(
        uiState = uiState,
        onBackClick = onBackClick
    )
}