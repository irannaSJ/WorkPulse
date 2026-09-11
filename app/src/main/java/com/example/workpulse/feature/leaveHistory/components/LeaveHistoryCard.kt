package com.example.workpulse.feature.leaveHistory.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BeachAccess
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.Flight
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.MoneyOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workpulse.core.ui.components.StatusChip
import com.example.workpulse.core.ui.theme.Dimens
import com.example.workpulse.core.ui.theme.WorkPulseShapes
import com.example.workpulse.feature.leaveHistory.LeaveHistoryCardUi

@Composable
fun LeaveHistoryCard(

    leave: LeaveHistoryCardUi,

    onClick: () -> Unit,

    modifier: Modifier = Modifier

) {

    Card(

        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },

        shape = WorkPulseShapes.large,

        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )

    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Space20),

            verticalAlignment = Alignment.CenterVertically

        ) {

            LeaveTypeIcon(
                leaveType = leave.leaveType
            )

            Spacer(modifier = Modifier.width(Dimens.Space16))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(

                    text = leave.leaveType,

                    style = MaterialTheme.typography.titleMedium,

                    fontWeight = FontWeight.Bold

                )

                Spacer(modifier = Modifier.height(Dimens.Space4))

                Text(

                    text = "${leave.fromDate} • ${leave.toDate}",

                    style = MaterialTheme.typography.bodySmall,

                    color = MaterialTheme.colorScheme.onSurfaceVariant

                )

                Spacer(modifier = Modifier.height(Dimens.Space4))

                Text(

                    text = leave.duration,

                    style = MaterialTheme.typography.labelMedium,

                    color = MaterialTheme.colorScheme.primary

                )

                Spacer(modifier = Modifier.height(Dimens.Space4))

                Text(

                    text = leave.reason,

                    style = MaterialTheme.typography.bodyMedium,

                    color = MaterialTheme.colorScheme.onSurfaceVariant,

                    maxLines = 2

                )

            }

            Spacer(modifier = Modifier.width(Dimens.Space12))

            Column(

                horizontalAlignment = Alignment.End

            ) {

                StatusChip(
                    status = leave.status
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(

                    text = "Applied On",

                    style = MaterialTheme.typography.labelSmall,

                    color = MaterialTheme.colorScheme.onSurfaceVariant

                )

                Text(

                    text = leave.appliedOn,

                    style = MaterialTheme.typography.bodySmall

                )

            }

        }

    }

}


@Composable
private fun LeaveTypeIcon(
    leaveType: String
) {

    val icon = when (leaveType) {

        "Casual Leave" ->
            Icons.Outlined.BeachAccess

        "Sick Leave" ->
            Icons.Outlined.LocalHospital

        "Privilege Leave" ->
            Icons.Outlined.Flight

        "Compensatory Off" ->
            Icons.Outlined.EventAvailable

        else ->
            Icons.Outlined.MoneyOff

    }

    Surface(

        modifier = Modifier.size(60.dp),

        shape = CircleShape,

        color = MaterialTheme.colorScheme.primaryContainer

    ) {

        Box(
            contentAlignment = Alignment.Center
        ) {

            Icon(

                imageVector = icon,

                contentDescription = null,

                tint = MaterialTheme.colorScheme.primary

            )

        }

    }

}
