package com.example.workpulse.feature.leaveHistory.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workpulse.core.ui.theme.WorkPulseTheme


@Composable
fun StatusFilterRow(

    selectedFilter: LeaveFilter,

    onFilterSelected: (LeaveFilter) -> Unit,

    modifier: Modifier = Modifier

) {

    Row(

        modifier = modifier
            .horizontalScroll(
                rememberScrollState()
            ),

        horizontalArrangement = Arrangement.spacedBy(12.dp)

    ) {

        LeaveFilter.entries.forEach { filter ->

            FilterChip(

                selected = filter == selectedFilter,

                onClick = {

                    onFilterSelected(filter)

                },

                label = {

                    Text(filter.title)

                },

                colors = FilterChipDefaults.filterChipColors(

                    selectedContainerColor =
                        MaterialTheme.colorScheme.primaryContainer,

                    selectedLabelColor =
                        MaterialTheme.colorScheme.primary

                )

            )

        }

    }

}






enum class LeaveFilter (
    val title: String
){
    ALL("All"),

    APPROVED("Approved"),

    PENDING("Pending"),

    REJECTED("Rejected"),

    CANCELLED("Cancelled")
}



@Preview(showBackground = true)
@Composable
private fun StatusFilterRowPreview() {

    WorkPulseTheme {

        StatusFilterRow(

            selectedFilter = LeaveFilter.ALL,

            onFilterSelected = {},

            modifier = Modifier.padding(16.dp)

        )

    }

}