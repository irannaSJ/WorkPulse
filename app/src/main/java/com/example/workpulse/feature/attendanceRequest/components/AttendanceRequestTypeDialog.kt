package com.example.workpulse.feature.attendanceRequest.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.HomeWork
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workpulse.feature.attendanceRequest.AttendanceRequestType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceRequestTypeDialog(

    selectedType: AttendanceRequestType?,

    onSelected: (AttendanceRequestType) -> Unit,

    onDismiss: () -> Unit

) {

    ModalBottomSheet(

        onDismissRequest = onDismiss

    ) {

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),

            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {

            Text(

                text = "Select Request Type",

                style = MaterialTheme.typography.headlineSmall,

                fontWeight = FontWeight.Bold

            )

            Divider()

            AttendanceRequestType.entries.forEach { type ->

                RequestTypeItem(

                    type = type,

                    selected = selectedType == type,

                    onClick = {

                        onSelected(type)

                    }

                )

            }

        }

    }

}


@Composable
private fun RequestTypeItem(

    type: AttendanceRequestType,

    selected: Boolean,

    onClick: () -> Unit

) {

    OutlinedCard(

        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),

        shape = RoundedCornerShape(16.dp),

        colors = CardDefaults.outlinedCardColors(

            containerColor =
                if (selected)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surface

        )

    ) {

        androidx.compose.foundation.layout.Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),

            verticalAlignment = Alignment.CenterVertically

        ) {

            Icon(

                imageVector = when (type) {

                    AttendanceRequestType.ON_DUTY ->
                        Icons.Outlined.CheckCircle

                    AttendanceRequestType.WORK_FROM_HOME ->
                        Icons.Outlined.HomeWork

                },

                contentDescription = null,

                tint = MaterialTheme.colorScheme.primary

            )

            androidx.compose.foundation.layout.Spacer(
                modifier = Modifier.weight(1f)
            )

            Text(

                text = type.displayName,

                style = MaterialTheme.typography.titleMedium

            )

        }

    }

}