package com.example.workpulse.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BeachAccess
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LeaveSummaryCard(

//    totalLeaves: Int,
//
//    usedLeaves: Int,

    remainingLeaves: Int,

    onViewAllClick: () -> Unit = {}

) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(28.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )

    ) {

        Column(
            modifier = Modifier.padding(24.dp)
        ) {

            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,

                verticalAlignment = Alignment.CenterVertically

            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Outlined.BeachAccess,
                        contentDescription = null,
                        tint = Color(0xFF2563EB)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Leave Summary",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                }

                TextButton(
                    onClick = onViewAllClick
                ) {

                    Text("View All")

                }

            }

//            Spacer(modifier = Modifier.height(24.dp))

//            Row(
//                horizontalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//
//                LeaveStatCard(
//                    title = "Total",
//                    value = totalLeaves.toString(),
//                    background = Color(0xFFE8F1FF),
//                    textColor = Color(0xFF2563EB),
//                    modifier = Modifier.weight(1f)
//                )
//
//                LeaveStatCard(
//                    title = "Used",
//                    value = usedLeaves.toString(),
//                    background = Color(0xFFFFF3E0),
//                    textColor = Color(0xFFFF9800),
//                    modifier = Modifier.weight(1f)
//                )
//
//            }

            Spacer(modifier = Modifier.height(16.dp))

            LeaveStatCard(
                title = "Remaining Leaves",
                value = remainingLeaves.toString(),
                background = Color(0xFFE8F8EE),
                textColor = Color(0xFF22C55E),
                modifier = Modifier.fillMaxWidth()
            )

        }

    }

}

@Composable
private fun LeaveStatCard(

    title: String,

    value: String,

    background: Color,

    textColor: Color,

    modifier: Modifier = Modifier

) {

    Card(

        modifier = modifier,

        shape = RoundedCornerShape(20.dp),

        colors = CardDefaults.cardColors(
            containerColor = background
        )

    ) {

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),

            horizontalAlignment = Alignment.CenterHorizontally

        ) {

            Text(

                text = value,

                style = MaterialTheme.typography.headlineMedium,

                fontWeight = FontWeight.Bold,

                color = textColor

            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(

                text = title,

                style = MaterialTheme.typography.bodyMedium,

                color = Color.DarkGray

            )

        }

    }

}