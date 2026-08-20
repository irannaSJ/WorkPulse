package com.example.workpulse.feature.compOffApplicationsHistory

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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.workpulse.feature.attendanceRequestHistory.AttendanceRequestHistoryFilter
import com.example.workpulse.feature.attendanceRequestHistory.components.AttendanceRequestHistoryCard
import com.example.workpulse.feature.compOffApplicationsHistory.components.CompOffApplicationHistoryCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompOffApplicationHistoryScreen(
    uiState: CompOffApplicationHistoryUiState,
    onBackClick : () -> Unit,
    onFilterSelected : (CompOffApplicationHistoryFilter) -> Unit,
    onRequestClick : (CompOffApplicationHistoryCardUi) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Compensatory Off History"
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
        )

        CompOffApplicationHistoryFilterRow(
            selectedFilter = uiState.selectedFilter,
            onFilterSelected= onFilterSelected,
            modifier = Modifier.fillMaxWidth()
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                )
        )
        when{
            uiState.isLoading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    CircularProgressIndicator()

                }
            }

            uiState.compOffApplication.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
//                    Text(
//                        text = if (uiState.searchQuery.isBlank()) {
//                            "No attendance requests found."
//                        } else {
//                            "No matching attendance requests found."
//                        },
//                        style = MaterialTheme.typography.bodyLarge
//                    )
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
                        items = uiState.compOffApplication,
                        key = {
                            it.id
                        }
                    ) { request ->

                        CompOffApplicationHistoryCard(

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
private fun CompOffApplicationHistoryFilterRow(
    selectedFilter : CompOffApplicationHistoryFilter,
    onFilterSelected : (CompOffApplicationHistoryFilter)-> Unit,
    modifier: Modifier= Modifier
){

    androidx.compose.foundation.lazy.LazyRow(
        modifier = modifier,

        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        item {

            FilterChip(
                selected =
                    selectedFilter ==
                            CompOffApplicationHistoryFilter.ALL,

                onClick = {
                    onFilterSelected(
                        CompOffApplicationHistoryFilter.ALL
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
                            CompOffApplicationHistoryFilter.PENDING,

                onClick = {
                    onFilterSelected(
                        CompOffApplicationHistoryFilter.PENDING
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
                            CompOffApplicationHistoryFilter.APPROVED,

                onClick = {
                    onFilterSelected(
                        CompOffApplicationHistoryFilter.APPROVED
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
                            CompOffApplicationHistoryFilter.CANCELLED,

                onClick = {
                    onFilterSelected(
                        CompOffApplicationHistoryFilter.CANCELLED
                    )
                },

                label = {
                    Text("Cancelled")
                }
            )
        }
    }
}