package com.example.workpulse.feature.leaveApplication.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.BeachAccess
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material.icons.outlined.MoneyOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workpulse.feature.leaveApplication.LeaveSuggestionUi
import com.example.workpulse.feature.leaveApplication.LeaveType


@Composable
fun LeaveSuggestionItem(

    suggestion: LeaveSuggestionUi,

    onClick: (LeaveType) -> Unit,

    modifier: Modifier = Modifier

) {

    Card(

        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick(suggestion.leaveType)
            },

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
                .padding(16.dp),

            verticalAlignment = Alignment.CenterVertically,

            horizontalArrangement = Arrangement.SpaceBetween

        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(

                    imageVector = leaveTypeIcon(suggestion.leaveType),

                    contentDescription = null,

                    modifier = Modifier.size(28.dp),

                    tint = MaterialTheme.colorScheme.primary

                )

                Spacer(modifier = Modifier.padding(start = 16.dp))

                Column {

                    Text(

                        text = suggestion.leaveType?.displayName ?: "Unknown Leave",

                        style = MaterialTheme.typography.titleSmall,

                        fontWeight = FontWeight.SemiBold

                    )

                    Text(

                        text = "Available : ${formatDays(suggestion.availableDays)} Days",

                        style = MaterialTheme.typography.bodyMedium,

                        color = MaterialTheme.colorScheme.onSurfaceVariant

                    )

                }

            }

            Icon(

                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,

                contentDescription = null,

                tint = MaterialTheme.colorScheme.onSurfaceVariant

            )

        }

    }

}


private fun formatDays(days: Double): String {
    return if (days % 1 == 0.0) {
        days.toInt().toString()
    } else {
        days.toString()
    }
}


private fun leaveTypeIcon(
    leaveType: LeaveType?
): ImageVector {

    return when (leaveType) {

        LeaveType.CASUAL ->
            Icons.Outlined.BeachAccess

        LeaveType.SICK ->
            Icons.Outlined.LocalHospital

        LeaveType.PRIVILEGE ->
            Icons.Outlined.Luggage

        LeaveType.COMP_OFF ->
            Icons.Outlined.EventAvailable

        LeaveType.LEAVE_WITHOUT_PAY ->
            Icons.Outlined.MoneyOff

        null ->
            Icons.Outlined.EventAvailable
    }

}





@Preview(showBackground = true)
@Composable
private fun LeaveSuggestionItemPreview() {

    LeaveSuggestionItem(

        suggestion = LeaveSuggestionUi(

            leaveType = LeaveType.PRIVILEGE,

            availableDays = 12.0

        ),

        onClick = {}

    )

}