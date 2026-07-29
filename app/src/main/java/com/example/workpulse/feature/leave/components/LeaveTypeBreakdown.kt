package com.example.workpulse.feature.leave.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.LocalHospital
import androidx.compose.material.icons.outlined.Luggage
import androidx.compose.material.icons.outlined.WorkOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workpulse.feature.leave.LeaveSummaryUiState

@Composable
fun LeaveTypeBreakdown(

    uiState: LeaveSummaryUiState

) {

    val balance = uiState.leaveBalance

    Column {

        Text(
            text = "Leave Type Breakdown",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LeaveTypeCard(
            title = "Casual Leave",
            balance = balance?.casualLeave ?: 0.0,
            icon = Icons.Outlined.Luggage,
            color = Color(0xFF2563EB)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LeaveTypeCard(
            title = "Sick Leave",
            balance = balance?.sickLeave ?: 0.0,
            icon = Icons.Outlined.LocalHospital,
            color = Color(0xFF22C55E)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LeaveTypeCard(
            title = "Privilege Leave",
            balance = balance?.privilegeLeave ?: 0.0,
            icon = Icons.Outlined.WorkOutline,
            color = Color(0xFF7C3AED)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LeaveTypeCard(
            title = "Compensatory Off",
            balance = balance?.compensatoryOff ?: 0.0,
            icon = Icons.Outlined.EventAvailable,
            color = Color(0xFFF97316)
        )

        Spacer(modifier = Modifier.height(12.dp))

        LeaveTypeCard(
            title = "Leave Without Pay",
            balance = balance?.leaveWithoutPay ?: 0.0,
            icon = Icons.Outlined.WorkOutline,
            color = Color(0xFFE11D48)
        )

    }

}

@Composable
private fun LeaveTypeCard(

    title: String,

    balance: Double,

    icon: ImageVector,

    color: Color

) {

    val progress =
        if (balance <= 0.0) 0f else 1f

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(22.dp),

        elevation = CardDefaults.cardElevation(4.dp)

    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),

            verticalAlignment = Alignment.CenterVertically

        ) {

            Card(

                shape = RoundedCornerShape(16.dp),

                colors = CardDefaults.cardColors(
                    containerColor = color.copy(alpha = 0.10f)
                )

            ) {

                Icon(

                    imageVector = icon,

                    contentDescription = null,

                    modifier = Modifier
                        .padding(14.dp)
                        .size(28.dp),

                    tint = color

                )

            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Available Balance",
                    color = Color.Gray,
                    fontSize = 15.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = color,
                    trackColor = Color(0xFFE8ECF3)
                )

            }

            Spacer(modifier = Modifier.width(18.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = balance.toInt().toString(),
                    fontWeight = FontWeight.Bold,
                    color = color,
                    fontSize = 24.sp
                )

                Text(
                    text = "Days",
                    color = Color.Gray,
                    fontSize = 13.sp
                )

            }

        }

    }

}