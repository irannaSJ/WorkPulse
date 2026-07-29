package com.example.workpulse.feature.home.presentation

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.verticalScroll

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

import com.example.workpulse.feature.home.presentation.components.DateTimeCard
import com.example.workpulse.feature.home.presentation.components.HomeTopBar


import com.example.workpulse.feature.home.presentation.components.AttendanceCard
import com.example.workpulse.feature.home.presentation.components.LeaveSummaryCard

enum class AttendanceState {
    NOT_PUNCHED_IN,
    PUNCHED_IN,
    PUNCHED_OUT
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onAttendanceClick: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onViewAllClick : () -> Unit
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        HomeTopBar(
            uiState = uiState,
            onProfileClick = onNavigateToProfile
        )

        Spacer(modifier = Modifier.height(20.dp))

        DateTimeCard()

        Spacer(modifier = Modifier.height(20.dp))

        AttendanceCard(
            attendanceState = uiState.attendanceState,
            workingSeconds = uiState.workingSeconds,
            punchInTime = uiState.punchInTime,
            punchOutTime = uiState.punchOutTime,
            onAttendanceClick = onAttendanceClick
        )

        Spacer(modifier = Modifier.height(20.dp))

        // LeaveSummaryCard() - We'll add this next.
        LeaveSummaryCard(
//            totalLeaves = 27,
//            usedLeaves = 6,
            remainingLeaves = uiState.remainingLeaves,
            onViewAllClick = onViewAllClick
        )

    }

}

fun formatWorkingTime(seconds: Long): String {

    val hrs = seconds / 3600
    val mins = (seconds % 3600) / 60
    val sec = seconds % 60

    return String.format(
        "%02d : %02d : %02d",
        hrs,
        mins,
        sec
    )
}