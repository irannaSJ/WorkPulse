package com.example.workpulse.feature.leave.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LeaveInfoCard() {

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8FAFC)
        )

    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),

            verticalAlignment = Alignment.CenterVertically

        ) {

            Icon(

                imageVector = Icons.Outlined.Info,

                contentDescription = null,

                tint = Color(0xFF3B82F6)

            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {

                Text(

                    text = "Information",

                    fontWeight = FontWeight.Bold

                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(

                    text = "Leave balances are synchronized with ERPNext. Offline changes will be updated automatically when the device reconnects.",

                    style = MaterialTheme.typography.bodyMedium,

                    color = Color.Gray

                )

            }

        }

    }

}