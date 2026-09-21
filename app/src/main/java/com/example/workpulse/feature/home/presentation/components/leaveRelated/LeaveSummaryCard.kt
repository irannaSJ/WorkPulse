package com.example.workpulse.feature.home.presentation.components.leaveRelated

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BeachAccess
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.example.workpulse.core.ui.theme.Dimens
import com.example.workpulse.core.ui.theme.WorkPulseShapes
import com.example.workpulse.feature.leave.LeaveSummaryUiState

@Composable
fun LeaveSummaryCard(
    modifier: Modifier = Modifier,
    remainingLeaves: Int,
    leaveUiState: LeaveSummaryUiState
) {
    val balance = leaveUiState.leaveBalance

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = WorkPulseShapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = Dimens.Elevation2
        ),
        border = BorderStroke(
            width = Dimens.DividerThickness,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
        )
    ) {
        Column(
            modifier = Modifier.padding(Dimens.Space16),
            verticalArrangement = Arrangement.spacedBy(Dimens.Space8)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.BeachAccess,
                    contentDescription = null,
                    modifier = Modifier.size(Dimens.Icon20),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(Dimens.Space8))

                Text(
                    text = "Leave Summary",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = remainingLeaves.toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(Dimens.Space4))

                Text(
                    text = "days",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.size(Dimens.Space4))

            LeaveTypeRow(
                title = "Casual Leave",
                balance = balance?.casualLeave ?: 0.0,
                icon = Icons.Outlined.Luggage,
                tint = MaterialTheme.colorScheme.primary
            )
            LeaveTypeDivider()
            LeaveTypeRow(
                title = "Sick Leave",
                balance = balance?.sickLeave ?: 0.0,
                icon = Icons.Outlined.LocalHospital,
                tint = MaterialTheme.colorScheme.secondary
            )
            LeaveTypeDivider()
            LeaveTypeRow(
                title = "Privilege Leave",
                balance = balance?.privilegeLeave ?: 0.0,
                icon = Icons.Outlined.WorkOutline,
                tint = MaterialTheme.colorScheme.tertiary
            )
            LeaveTypeDivider()
            LeaveTypeRow(
                title = "Compensatory Off",
                balance = balance?.compensatoryOff ?: 0.0,
                icon = Icons.Outlined.EventAvailable,
                tint = MaterialTheme.colorScheme.primary
            )
            LeaveTypeDivider()
            LeaveTypeRow(
                title = "Leave Without Pay",
                balance = balance?.leaveWithoutPay ?: 0.0,
                icon = Icons.Outlined.WorkOutline,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LeaveTypeRow(
    title: String,
    balance: Double,
    icon: ImageVector,
    tint: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.Space4),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.Icon28)
                .background(
                    color = tint.copy(alpha = 0.10f),
                    shape = RoundedCornerShape(Dimens.Radius8)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(Dimens.Icon16),
                tint = tint
            )
        }

        Spacer(modifier = Modifier.width(Dimens.Space12))

        Text(
            text = title,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Text(
            text = balance.toInt().toString(),
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun LeaveTypeDivider() {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.65f)
    )
}
