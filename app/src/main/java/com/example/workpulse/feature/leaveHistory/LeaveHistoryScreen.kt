package com.example.workpulse.feature.leaveHistory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
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
import com.example.workpulse.core.ui.theme.AdaptiveLayout
import com.example.workpulse.core.ui.theme.Dimens
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
        BoxWithConstraints(modifier = Modifier.weight(1f)) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = AdaptiveLayout.HistoryContentMaxWidth),

            contentPadding = PaddingValues(Dimens.Space16),

            verticalArrangement = Arrangement.spacedBy(Dimens.Space16)

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

    }

}




@Composable
fun EmptySearchResult(
    modifier: Modifier = Modifier
) {

    Column(

        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.Space64),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center

    ) {

        Icon(

            imageVector = Icons.Outlined.SearchOff,

            contentDescription = null,

            modifier = Modifier.size(Dimens.Icon56),

            tint = MaterialTheme.colorScheme.outline

        )

        Spacer(modifier = Modifier.height(Dimens.Space20))

        Text(

            text = "No Leave Applications Found",

            style = MaterialTheme.typography.titleMedium,

            fontWeight = FontWeight.SemiBold

        )

        Spacer(modifier = Modifier.height(Dimens.Space8))

        Text(

            text = "Try changing your search or selecting a different filter.",

            style = MaterialTheme.typography.bodyMedium,

            color = MaterialTheme.colorScheme.onSurfaceVariant

        )

    }

}
