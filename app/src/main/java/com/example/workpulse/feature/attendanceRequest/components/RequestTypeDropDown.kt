package com.example.workpulse.feature.attendanceRequest.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.WorkHistory
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import com.example.workpulse.feature.attendanceRequest.AttendanceRequestType

@Composable
fun RequestTypeDropDown(

    requestType: AttendanceRequestType?,

    onClick: () -> Unit,

    modifier: Modifier = Modifier

) {

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Text(
            text = "Request Type",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedCard(

            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),

            shape = RoundedCornerShape(16.dp),

            colors = CardDefaults.outlinedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )

        ) {

            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 18.dp
                    ),

                verticalAlignment = Alignment.CenterVertically

            ) {

                Icon(

                    imageVector = Icons.Outlined.WorkHistory,

                    contentDescription = null,

                    tint = MaterialTheme.colorScheme.primary

                )

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Text(

                    text = requestType?.displayName
                        ?: "Select Request Type",

                    modifier = Modifier.weight(1f),

                    style = MaterialTheme.typography.bodyLarge,

                    color =
                        if (requestType == null)
                            MaterialTheme.colorScheme.onSurfaceVariant
                        else
                            MaterialTheme.colorScheme.onSurface

                )

                Icon(

                    imageVector = Icons.Default.KeyboardArrowDown,

                    contentDescription = null,

                    tint = MaterialTheme.colorScheme.primary

                )

            }

        }

    }

}