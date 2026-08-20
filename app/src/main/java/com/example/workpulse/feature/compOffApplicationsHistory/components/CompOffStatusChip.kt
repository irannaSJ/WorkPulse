package com.example.workpulse.feature.compOffApplicationsHistory.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workpulse.data.local.entity.ApplicationStatus

@Composable
fun CompOffStatusChip(

    status: ApplicationStatus

) {

    val (containerColor, contentColor) = when (status) {

        ApplicationStatus.SUBMITTED ->

            Color(0xFFE8F5E9) to Color(0xFF2E7D32)

        ApplicationStatus.PENDING ->

            Color(0xFFFFF3E0) to Color(0xFFEF6C00)

        ApplicationStatus.REJECTED ->

            Color(0xFFFFEBEE) to Color(0xFFC62828)

        ApplicationStatus.CANCELLED ->

            Color(0xFFF3F4F6) to Color(0xFF616161)

    }

    AssistChip(

        onClick = {},

        enabled = false,

        label = {

            Text(

                text = status.displayName,

                style = MaterialTheme.typography.labelMedium,

                fontWeight = FontWeight.SemiBold

            )

        },

        colors = AssistChipDefaults.assistChipColors(

            containerColor = containerColor,

            labelColor = contentColor,

            disabledContainerColor = containerColor,

            disabledLabelColor = contentColor

        ),

        shape = RoundedCornerShape(50)

    )

}