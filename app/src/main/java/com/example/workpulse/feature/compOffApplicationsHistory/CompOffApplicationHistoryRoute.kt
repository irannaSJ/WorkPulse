package com.example.workpulse.feature.compOffApplicationsHistory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.workpulse.feature.compOffApplicationsHistory.components.CompOffDetailsBottomSheet


@Composable
fun CompOffApplicationHistoryRoute(
    onBackClick : () -> Unit,
) {
    val viewModel : CompOffApplicationHistoryViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CompOffApplicationHistoryScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onFilterSelected = viewModel::onFilterChanged,
        onRequestClick = viewModel::onCompOffClicked
    )

    if(uiState.showBottomSheet){
        uiState.compOffDetails?.let { leave ->
            CompOffDetailsBottomSheet(
                leave = leave,
                onDismiss = viewModel::hideBottomSheet
            )
        }
    }
}