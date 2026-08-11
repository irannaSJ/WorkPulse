package com.example.workpulse.feature.attendanceRequestHistory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.workpulse.core.worker.SyncScheduler
import com.example.workpulse.feature.attendanceRequestHistory.components.AttendanceRequestHistoryDetailsBottomSheet

@Composable
fun AttendanceRequestHistoryRoute(

    onBackClick: () -> Unit

) {

    val viewModel: AttendanceRequestHistoryViewModel =
        hiltViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

//    val context = LocalContext.current
//
//    LaunchedEffect(Unit) {
//
//        SyncScheduler(context)
//            .scheduleAttendanceRequestSync()
//
//    }

    AttendanceRequestHistoryScreen(

        uiState = uiState,

        onBackClick = onBackClick,

        onSearchQueryChange =
            viewModel::onSearchQueryChanged,

        onFilterSelected =
            viewModel::onFilterChanged,

        onRequestClick =
            viewModel::onRequestClicked
    )


    if(uiState.showDetailsBottomSheet){
        uiState.selectedRequest?.let { request ->
            AttendanceRequestHistoryDetailsBottomSheet(
                request = request,
                onDismiss = viewModel::hideDetailsBottomSheet
            )
        }
    }

}