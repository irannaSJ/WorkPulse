package com.example.workpulse.feature.leave

import com.example.workpulse.feature.leave.components.LeaveSummaryTopBar
import com.example.workpulse.feature.leave.components.EmployeeHeaderCard
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.*
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
import java.io.File
import com.example.workpulse.R
import com.example.workpulse.feature.leave.components.LeaveBalanceSection
import com.example.workpulse.feature.leave.components.LeaveInfoCard
import com.example.workpulse.feature.leave.components.LeaveTypeBreakdown


@Composable
fun LeaveSummaryScreen(

    onBackClick: () -> Unit,
    uiState: LeaveSummaryUiState,



) {

    Scaffold(

        containerColor = Color(0xFFF8FAFC)

    ) { padding ->

        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())

        ) {

            Spacer(Modifier.height(16.dp))

            LeaveSummaryTopBar(
                onBackClick = onBackClick
            )

            Spacer(Modifier.height(24.dp))

            EmployeeHeaderCard(
                uiState = uiState
            )

            Spacer(Modifier.height(24.dp))

            LeaveBalanceSection(
                uiState = uiState
            )

            Spacer(modifier = Modifier.height(24.dp))

            LeaveTypeBreakdown(
                uiState = uiState
            )

            Spacer(modifier = Modifier.height(24.dp))

            LeaveInfoCard()

            Spacer(modifier = Modifier.height(24.dp))

        }

    }

}