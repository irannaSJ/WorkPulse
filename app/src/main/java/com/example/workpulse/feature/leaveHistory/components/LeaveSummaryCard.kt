package com.example.workpulse.feature.leaveHistory.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workpulse.core.ui.theme.WorkPulseTheme
import com.example.workpulse.feature.leaveHistory.LeaveSummaryUi

@Composable
fun LeaveSummaryCard(

    summary: LeaveSummaryUi,

    modifier: Modifier = Modifier

) {

    Card(

        modifier = modifier.fillMaxWidth(),

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )

    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 20.dp
                ),

            horizontalArrangement = Arrangement.SpaceEvenly,

            verticalAlignment = Alignment.CenterVertically

        ) {

            SummaryItem(
                title = "Total",
                count = summary.total
            )

            SummaryItem(
                title = "Approved",
                count = summary.approved
            )

            SummaryItem(
                title = "Pending",
                count = summary.pending
            )

            SummaryItem(
                title = "Rejected",
                count = summary.rejected
            )

            SummaryItem(
                title = "Cancelled",
                count = summary.cancelled
            )

        }

    }

}



@Composable
private fun SummaryItem(

    title: String,

    count: Int,

    modifier: Modifier = Modifier

) {

    Column(

        modifier = modifier,

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.spacedBy(6.dp)

    ) {

        Text(

            text = title,

            style = MaterialTheme.typography.labelSmall,

            color = MaterialTheme.colorScheme.onSurfaceVariant

        )

        Text(

            text = count.toString(),

            style = MaterialTheme.typography.titleLarge,

            fontWeight = FontWeight.Bold,

            color = MaterialTheme.colorScheme.onSurface

        )

    }

}

