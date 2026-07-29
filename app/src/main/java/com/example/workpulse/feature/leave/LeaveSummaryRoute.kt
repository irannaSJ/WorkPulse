package com.example.workpulse.feature.leave

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LeaveSummaryRoute(
    onBackClick: () -> Unit,
    viewModel: LeaveSummaryViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LeaveSummaryScreen(
        uiState = uiState,
        onBackClick = onBackClick
    )
}