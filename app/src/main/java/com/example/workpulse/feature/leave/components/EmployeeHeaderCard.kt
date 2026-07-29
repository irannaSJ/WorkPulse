package com.example.workpulse.feature.leave.components

import androidx.compose.foundation.Image
import com.example.workpulse.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.workpulse.feature.leave.LeaveSummaryUiState
import java.io.File

@Composable
fun EmployeeHeaderCard(

    uiState: LeaveSummaryUiState

) {

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp),

        shape = RoundedCornerShape(28.dp),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),

        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )

    ) {

        Box(

            modifier = Modifier
                .fillMaxSize()
                .background(

                    Brush.horizontalGradient(

                        listOf(

                            Color(0xFF7C9CFF),

                            Color(0xFFC6D4FF)

                        )

                    )

                )

        ) {

            Row(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),

                verticalAlignment = Alignment.CenterVertically

            ) {

                Card(

                    modifier = Modifier.size(88.dp),

                    shape = CircleShape,

                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )

                ) {

                    if (uiState.employeeImage.isNotBlank()) {

                        AsyncImage(

                            model = File(uiState.employeeImage),

                            contentDescription = null,

                            modifier = Modifier.fillMaxSize(),

                            contentScale = ContentScale.Crop

                        )

                    } else {

                        Image(

                            painter = painterResource(R.drawable.profile_placeholder),

                            contentDescription = null,

                            modifier = Modifier.fillMaxSize(),

                            contentScale = ContentScale.Crop

                        )

                    }

                }

                Spacer(modifier = Modifier.width(20.dp))

                Column(

                    modifier = Modifier.weight(1f),

                    verticalArrangement = Arrangement.Center

                ) {

                    Text(

                        text = uiState.employeeName,

                        fontSize = 30.sp,

                        fontWeight = FontWeight.Bold,

                        color = Color(0xFF111827)

                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(

                        text = uiState.designation,

                        style = MaterialTheme.typography.titleMedium,

                        color = Color(0xFF1F2937)

                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(

                        text = "Employee ID: ${uiState.employeeId}",

                        style = MaterialTheme.typography.bodyLarge,

                        color = Color(0xFF374151)

                    )

                }

            }

            Icon(

                imageVector = Icons.Outlined.CalendarMonth,

                contentDescription = null,

                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 28.dp)
                    .size(90.dp),

                tint = Color.White.copy(alpha = 0.25f)

            )

        }

    }

}