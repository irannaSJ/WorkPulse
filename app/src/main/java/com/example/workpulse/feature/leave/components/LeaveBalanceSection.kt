package com.example.workpulse.feature.leave.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workpulse.feature.leave.LeaveSummaryUiState

@Composable
fun LeaveBalanceSection(

    uiState: LeaveSummaryUiState

) {

    val balance = uiState.leaveBalance

    val casual = balance?.casualLeave ?: 0.0
    val sick = balance?.sickLeave ?: 0.0
    val privilege = balance?.privilegeLeave ?: 0.0
    val compensatory = balance?.compensatoryOff ?: 0.0
    val lwp = balance?.leaveWithoutPay ?: 0.0

    val totalLeaves =
        casual +
                sick +
                privilege +
                compensatory +
                lwp

    val remainingLeaves = totalLeaves

    // ERPNext report doesn't return used leaves
    val usedLeaves = 0.0

    Column {

        Text(
            text = "Leave Balance Overview",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            LeaveBalanceCard(
                modifier = Modifier.weight(1f),
                title = "Total Leaves",
                value = totalLeaves.toInt(),
                icon = Icons.Outlined.CalendarMonth,
                numberColor = Color(0xFF2563EB),
                background = listOf(
                    Color(0xFFF5F9FF),
                    Color(0xFFEAF2FF)
                )
            )

            LeaveBalanceCard(
                modifier = Modifier.weight(1f),
                title = "Used Leaves",
                value = usedLeaves.toInt(),
                icon = Icons.Outlined.QueryStats,
                numberColor = Color(0xFF22C55E),
                background = listOf(
                    Color(0xFFF5FFF8),
                    Color(0xFFEFFBF3)
                )
            )

            LeaveBalanceCard(
                modifier = Modifier.weight(1f),
                title = "Remaining",
                value = remainingLeaves.toInt(),
                icon = Icons.Outlined.PieChart,
                numberColor = Color(0xFF7C3AED),
                background = listOf(
                    Color(0xFFF9F5FF),
                    Color(0xFFF3ECFF)
                )
            )

        }

    }

}

@Composable
private fun LeaveBalanceCard(

    modifier: Modifier = Modifier,

    title: String,

    value: Int,

    icon: ImageVector,

    numberColor: Color,

    background: List<Color>

) {

    Card(

        modifier = modifier.height(210.dp),

        shape = RoundedCornerShape(22.dp),

        elevation = CardDefaults.cardElevation(3.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )

    ) {

        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(background)
                )
                .padding(18.dp),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.SpaceBetween

        ) {

            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                color = numberColor,
                fontSize = 13.sp
            )

            Text(
                text = value.toString(),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = numberColor
            )

            Text(
                text = "Days",
                fontSize = 18.sp,
                color = Color.DarkGray
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(16.dp)
                        .size(34.dp),
                    tint = numberColor
                )

            }

        }

    }

}