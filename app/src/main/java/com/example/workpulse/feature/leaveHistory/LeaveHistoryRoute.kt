package com.example.workpulse.feature.leaveHistory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.workpulse.feature.leaveHistory.components.LeaveDetailsBottomSheet

@Composable
fun LeaveHistoryRoute(

    onBackClick: () -> Unit,

    onLeaveClick: (String) -> Unit = {}

) {

    val viewModel: LeaveHistoryViewModel =
        hiltViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

//    LeaveHistoryScreen(
//
//        summary = uiState.summary,
//
//        searchQuery = uiState.searchQuery,
//
//        selectedFilter = uiState.selectedFilter,
//
//        leaveApplications = uiState.leaveApplications,
//
//        onBackClick = onBackClick,
//
//        onSearchQueryChange = viewModel::onSearchQueryChanged,
//
//        onFilterSelected = viewModel::onFilterChanged,
//
//        onLeaveClick = {
//            // We'll connect this later
//        }
//
//    )

    LeaveHistoryScreen(

        summary = uiState.summary,

        searchQuery = uiState.searchQuery,

        selectedFilter = uiState.selectedFilter,

        leaveApplications = uiState.leaveApplications,

        onBackClick = onBackClick,

        onSearchQueryChange = viewModel::onSearchQueryChanged,

        onFilterSelected = viewModel::onFilterChanged,

        onLeaveClick = viewModel::onLeaveClicked

    )

    if (

        uiState.showBottomSheet &&

        uiState.leaveDetails != null

    ) {

        LeaveDetailsBottomSheet(

            leave = uiState.leaveDetails,

            onDismiss = viewModel::hideBottomSheet

        )

    }

}