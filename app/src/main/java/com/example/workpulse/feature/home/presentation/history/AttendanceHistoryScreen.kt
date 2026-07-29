package com.example.workpulse.feature.home.presentation.history


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceHistoryScreen(
    uiState: AttendanceHistoryUiState,
    onBackClick: () -> Unit = {},
    onCalendarClick: () -> Unit = {}
) {

    val attendanceList = uiState.attendanceList
    var selectedAttendance by remember {
        mutableStateOf<AttendanceHistoryUiModel?>(null)
    }



    Scaffold(
        containerColor = Color(0xFFF6F8FC)
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {

            AttendanceTopBar(
                onBackClick,
                onCalendarClick
            )

            Spacer(modifier = Modifier.height(20.dp))

            MonthlySummaryCard(

                month = uiState.currentMonth,

                presentDays = uiState.presentDays,

                totalHours = uiState.totalWorkingHours,

                averageHours = uiState.averageWorkingHours

            )

            Spacer(modifier = Modifier.height(20.dp))

            AttendanceHeader()

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {

                items(attendanceList) { attendance ->

                    AttendanceHistoryCard(
                        attendance = attendance,
                        onClick = { selected ->
                            selectedAttendance = selected
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                item {

                    BottomInfoCard()

                }

            }

        }

    }

    selectedAttendance?.let {

        AttendanceDetailsBottomSheet(

            attendance = it,

            onDismiss = {

                selectedAttendance = null

            }

        )

    }

}


@Composable
private fun AttendanceTopBar(

    onBackClick: () -> Unit,

    onCalendarClick: () -> Unit

) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 20.dp),

        verticalAlignment = Alignment.CenterVertically

    ) {

        FilledTonalIconButton(
            onClick = onBackClick
        ) {

            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                null
            )

        }

        Spacer(modifier = Modifier.weight(0.6f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                "Attendance History",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Your attendance records",
                color = Color.Gray
            )

        }

        Spacer(modifier = Modifier.weight(1f))

//        FilledTonalIconButton(
//            onClick = onCalendarClick
//        ) {
//
//            Icon(
//                Icons.Outlined.CalendarMonth,
//                null
//            )
//
//        }

    }

}


@Composable
private fun MonthlySummaryCard(
    month : String,
    presentDays : Int,
    totalHours : String,
    averageHours : String
) {

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),

        shape = RoundedCornerShape(24.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )

    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),

            verticalAlignment = Alignment.CenterVertically

        ) {

            Box(

                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF2F5FF)),

                contentAlignment = Alignment.Center

            ) {

                Icon(
                    Icons.Outlined.CalendarMonth,
                    null,
                    tint = Color(0xFF3F6DF6)
                )

            }

            Spacer(modifier = Modifier.width(20.dp))

            SummaryItem(
                "This Month",
                month,
                Color.Black
            )

            Spacer(modifier = Modifier.weight(1f))

            SummaryItem(
                "Present",
                presentDays.toString(),
                Color(0xFF22C55E)
            )

            Spacer(modifier = Modifier.weight(1f))

            SummaryItem(
                "Hours",
                totalHours,
                Color(0xFF3F6DF6)
            )

            Spacer(modifier = Modifier.weight(1f))

            SummaryItem(
                "Average",
                averageHours,
                Color(0xFF8B5CF6)
            )

        }

    }

}



@Composable
private fun SummaryItem(

    title: String,

    value: String,

    valueColor: Color

) {

    Column(

        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text(
            title,
            color = Color.Gray,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            value,
            color = valueColor,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

    }

}


@Composable
private fun AttendanceHeader() {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),

        horizontalArrangement = Arrangement.SpaceBetween

    ) {

        Text(
            "Date",
            fontWeight = FontWeight.Bold
        )

        Text(
            "Punch In",
            fontWeight = FontWeight.Bold
        )

        Text(
            "Punch Out",
            fontWeight = FontWeight.Bold
        )

        Text(
            "Hours",
            fontWeight = FontWeight.Bold
        )

    }

}


@Composable
private fun BottomInfoCard() {

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )

    ) {

        Text(

            modifier = Modifier.padding(16.dp),

            text = "All times are based on your local time zone.",

            color = Color.Gray

        )

    }


}
