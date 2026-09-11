package com.example.workpulse.feature.home.presentation.components.leaveRelated

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BeachAccess
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workpulse.core.ui.theme.Dimens
import com.example.workpulse.core.ui.theme.WorkPulseShapes
import com.example.workpulse.feature.leave.LeaveSummaryUiState
import com.example.workpulse.feature.leave.components.LeaveTypeBreakdown

@Composable
fun LeaveSummaryCard(

    modifier: Modifier = Modifier,

    remainingLeaves: Int,
    leaveUiState : LeaveSummaryUiState

) {

    Card(

        modifier = modifier.fillMaxWidth(),

        shape = WorkPulseShapes.extraLarge,

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens.cardElevation
        )

    ) {

        Column(
            modifier = Modifier.padding(Dimens.Space24)
        ) {

            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,

                verticalAlignment = Alignment.CenterVertically

            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Outlined.BeachAccess,
                        contentDescription = null,
                        tint = Color(0xFF2563EB)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Leave Summary",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            LeaveStatCard(
                title = "Remaining Leaves",
                value = remainingLeaves.toString(),
                background = Color(0xFFE8F8EE),
                textColor = Color(0xFF22C55E),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LeaveTypeBreakdown(uiState =leaveUiState)

        }

    }

}

@Composable
private fun LeaveStatCard(

    title: String,

    value: String,

    background: Color,

    textColor: Color,

    modifier: Modifier = Modifier

) {

    Card(

        modifier = modifier,

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = background
        )

    ) {

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Text(

                text = value,

                style = MaterialTheme.typography.headlineMedium,

                fontWeight = FontWeight.Bold,

                color = textColor

            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(

                text = title,

                style = MaterialTheme.typography.bodyMedium,

                color = Color.DarkGray

            )

        }

    }

}
