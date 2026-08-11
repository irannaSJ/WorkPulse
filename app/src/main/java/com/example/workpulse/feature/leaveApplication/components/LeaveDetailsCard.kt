package com.example.workpulse.feature.leaveApplication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workpulse.feature.leaveApplication.LeaveApplicationUiState

@Composable
fun LeaveDetailsCard(
    uiState: LeaveApplicationUiState,
    onLeaveTypeClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    ElevatedCard(
        modifier = modifier.fillMaxWidth()
            .background(MaterialTheme.colorScheme.background),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            LeaveDropDownField(
                label = "Leave Type",
                value = uiState.leaveType?.displayName?:"",
                placeholder = "Select Leave Type",
                onClick = onLeaveTypeClick
            )

            ReadOnlyField(
                label = "Employee",
                value = uiState.employeeName,
                placeholder = "Loading Employee...",
                leadingIcon = Icons.Outlined.Person
            )

            ReadOnlyField(
                label = "Company",
                value = uiState.company,
                placeholder = "Loading Company...",
                leadingIcon = Icons.Outlined.Business
            )

        }

    }

}


@Composable
fun LeaveDropDownField(
    label: String,
    value: String,
    placeholder: String,
    availableBalance: Double? = null,
    onClick: () -> Unit,
    modifier : Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

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
                    imageVector = Icons.Outlined.EventAvailable,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = if (value.isBlank()) placeholder else value,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge,
                    color =
                        if (value.isBlank())
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

        availableBalance?.let {

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(50),
                color = when {

                    it <= 0.0 ->
                        MaterialTheme.colorScheme.errorContainer

                    it <= 2.0 ->
                        MaterialTheme.colorScheme.tertiaryContainer

                    else ->
                        MaterialTheme.colorScheme.primaryContainer

                }
            ) {

                Text(
                    text = "Available Balance : $it Days",
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 6.dp
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = when {

                        it <= 0.0 ->
                            MaterialTheme.colorScheme.error

                        it <= 2.0 ->
                            Color(0xFFB26A00)

                        else ->
                            Color(0xFF2E7D32)

                    }
                )

            }

        }

    }

}


@Composable
fun ReadOnlyField(
    label: String,
    value: String,
    placeholder: String,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,

        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            enabled = false,
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            leadingIcon = {

                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null
                )

            },
            placeholder = {
                Text(
                    text = placeholder,
                    color = MaterialTheme.colorScheme.onSurface
                )

            }
        )

    }

}