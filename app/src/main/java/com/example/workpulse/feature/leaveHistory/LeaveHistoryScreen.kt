package com.example.workpulse.feature.leaveHistory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workpulse.feature.leaveHistory.components.LeaveFilter
import com.example.workpulse.feature.leaveHistory.components.LeaveHistoryCard

import com.example.workpulse.feature.leaveHistory.components.LeaveHistoryTopBar
import com.example.workpulse.feature.leaveHistory.components.LeaveSummaryCard
import com.example.workpulse.feature.leaveHistory.components.SearchBar
import com.example.workpulse.feature.leaveHistory.components.StatusFilterRow

@Composable
fun LeaveHistoryScreen(

    summary: LeaveSummaryUi,

    searchQuery: String,

    selectedFilter: LeaveFilter,

    leaveApplications: List<LeaveHistoryCardUi>,

    onBackClick: () -> Unit,

    onSearchQueryChange: (String) -> Unit,

    onFilterSelected: (LeaveFilter) -> Unit,

    onLeaveClick: (LeaveHistoryCardUi) -> Unit,

    modifier: Modifier = Modifier

) {

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        LeaveHistoryTopBar(
            onBackClick = onBackClick
        )
        LazyColumn(
            modifier = Modifier.weight(1f),

            contentPadding = PaddingValues(16.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {

            // Search Bar
            item {

                SearchBar(

                    query = searchQuery,

                    onQueryChange = onSearchQueryChange

                )

            }

            // Status Filter Chips
            item {

                StatusFilterRow(

                    selectedFilter = selectedFilter,

                    onFilterSelected = onFilterSelected

                )

            }

            // Summary Card
            item {

                LeaveSummaryCard(

                    summary = summary

                )

            }

//            // Leave History Cards
//            items(
//                leaveApplications
//            ) { leave ->
//
//                LeaveHistoryCard(
//
//                    leave = leave,
//
//                    onClick = {
//
//                        onLeaveClick(leave)
//
//                    }
//
//                )
//
//            }

            if (leaveApplications.isEmpty()) {

                item {

                    EmptySearchResult()

                }

            } else {

                items(leaveApplications) { leave ->

                    LeaveHistoryCard(

                        leave = leave,

                        onClick = {

                            onLeaveClick(leave)

                        }

                    )

                }

            }

        }

    }

}




@Composable
fun EmptySearchResult(
    modifier: Modifier = Modifier
) {

    Column(

        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center

    ) {

        Icon(

            imageVector = Icons.Outlined.SearchOff,

            contentDescription = null,

            modifier = Modifier.size(72.dp),

            tint = MaterialTheme.colorScheme.outline

        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(

            text = "No Leave Applications Found",

            style = MaterialTheme.typography.titleMedium,

            fontWeight = FontWeight.SemiBold

        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(

            text = "Try changing your search or selecting a different filter.",

            style = MaterialTheme.typography.bodyMedium,

            color = MaterialTheme.colorScheme.onSurfaceVariant

        )

    }

}
