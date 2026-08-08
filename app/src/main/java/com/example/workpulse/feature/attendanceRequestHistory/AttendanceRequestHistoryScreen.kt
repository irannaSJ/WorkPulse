package com.example.workpulse.feature.attendanceRequestHistory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.unit.dp
import com.example.workpulse.feature.attendanceRequestHistory.components.AttendanceRequestHistoryCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceRequestHistoryScreen(
    uiState: AttendanceRequestHistoryUiState,
    onBackClick: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onFilterSelected: (AttendanceRequestHistoryFilter) -> Unit,
    onRequestClick: (AttendanceRequestHistoryCardUi) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // --------------------------------
        // Header
        // --------------------------------

        CenterAlignedTopAppBar(

            title = {
                Text(
                    text = "Attendance Request History"
                )
            },

            navigationIcon = {

                IconButton(
                    onClick = onBackClick
                ) {

                    Icon(
                        imageVector =
                            Icons.AutoMirrored.Filled.ArrowBack,

                        contentDescription = "Back"
                    )
                }
            },

            colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
        )

        // --------------------------------
        // Search
        // --------------------------------

        OutlinedTextField(
            value = uiState.searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            label = {
                Text("Search requests")
            },
            singleLine = true
        )

        // --------------------------------
        // Filter
        // --------------------------------

        AttendanceRequestHistoryFilterRow(
            selectedFilter = uiState.selectedFilter,
            onFilterSelected = onFilterSelected,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                )
        )

        // --------------------------------
        // Content
        // --------------------------------

        when {

            uiState.isLoading -> {

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    CircularProgressIndicator()

                }
            }

            uiState.attendanceRequests.isEmpty() -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = if (uiState.searchQuery.isBlank()) {
                            "No attendance requests found."
                        } else {
                            "No matching attendance requests found."
                        },
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            else -> {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),

                    contentPadding = PaddingValues(
                        horizontal = 20.dp,
                        vertical = 8.dp
                    ),

                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(
                        items = uiState.attendanceRequests,
                        key = {
                            it.id
                        }
                    ) { request ->

                        AttendanceRequestHistoryCard(

                            request = request,

                            onClick = {
                                onRequestClick(request)
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun AttendanceRequestHistoryFilterRow(
    selectedFilter: AttendanceRequestHistoryFilter,
    onFilterSelected: (AttendanceRequestHistoryFilter) -> Unit,
    modifier: Modifier = Modifier
) {

    androidx.compose.foundation.lazy.LazyRow(
        modifier = modifier,

        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item {

            FilterChip(
                selected =
                    selectedFilter ==
                            AttendanceRequestHistoryFilter.ALL,

                onClick = {
                    onFilterSelected(
                        AttendanceRequestHistoryFilter.ALL
                    )
                },

                label = {
                    Text("All")
                }
            )
        }

        item {

            FilterChip(
                selected =
                    selectedFilter ==
                            AttendanceRequestHistoryFilter.PENDING,

                onClick = {
                    onFilterSelected(
                        AttendanceRequestHistoryFilter.PENDING
                    )
                },

                label = {
                    Text("Pending")
                }
            )
        }

        item {

            FilterChip(
                selected =
                    selectedFilter ==
                            AttendanceRequestHistoryFilter.APPROVED,

                onClick = {
                    onFilterSelected(
                        AttendanceRequestHistoryFilter.APPROVED
                    )
                },

                label = {
                    Text("Approved")
                }
            )
        }

        item {

            FilterChip(
                selected =
                    selectedFilter ==
                            AttendanceRequestHistoryFilter.REJECTED,

                onClick = {
                    onFilterSelected(
                        AttendanceRequestHistoryFilter.REJECTED
                    )
                },

                label = {
                    Text("Rejected")
                }
            )

        }

        item {

            FilterChip(
                selected =
                    selectedFilter ==
                            AttendanceRequestHistoryFilter.CANCELLED,

                onClick = {
                    onFilterSelected(
                        AttendanceRequestHistoryFilter.CANCELLED
                    )
                },

                label = {
                    Text("Cancelled")
                }
            )
        }
    }
}