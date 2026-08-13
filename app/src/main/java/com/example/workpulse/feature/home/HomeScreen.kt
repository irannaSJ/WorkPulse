package com.example.workpulse.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope

import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

import com.example.workpulse.feature.attendance.DateTimeCard
import com.example.workpulse.feature.home.presentation.components.HomeTopBar


import com.example.workpulse.feature.attendance.AttendanceCard
import com.example.workpulse.feature.home.presentation.components.leaveRelated.LeaveSummaryCard


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.workpulse.feature.home.presentation.components.drawerRelated.LogoutDialog
import com.example.workpulse.feature.home.presentation.components.drawerRelated.NavigationDrawerContent
import com.example.workpulse.feature.leave.LeaveSummaryUiState
import kotlinx.coroutines.launch

enum class AttendanceState {
    NOT_PUNCHED_IN,
    PUNCHED_IN,
    PUNCHED_OUT
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onAttendanceClick: () -> Unit,
    onProfileClick : () -> Unit,
    onLeaveClick : () -> Unit,
    onAttendanceHistoryClick : () -> Unit,
    onAttendanceRequestClick : () -> Unit,
    onLeaveHistoryClick : () -> Unit,
    onAttendanceRequestHistoryClick:() -> Unit,
    onCompOffApplicationClick : () -> Unit,
    onLogoutClick : () -> Unit,
    leaveUiState : LeaveSummaryUiState
) {
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    var showLogoutDialog by rememberSaveable {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()


    ModalNavigationDrawer(

        drawerState = drawerState,

        drawerContent = {

            NavigationDrawerContent(

                employeeName = uiState.employeeName,

                designation = uiState.designation,
                company = uiState.company,

                selectedRoute = "home",

                onHomeClick = {
                    scope.launch {
                        drawerState.close()
                    }
                },

                onProfileClick = {
                    scope.launch {
                        drawerState.close()
                        onProfileClick()
                    }

                },

                onLeaveClick = {
                    scope.launch {
                        drawerState.close()
                        onLeaveClick()
                    }

                },

                onAttendanceHistoryClick = {
                    scope.launch {
                        drawerState.close()
                        onAttendanceHistoryClick()
                    }

                },

                onAttendanceRequestClick = {
                    scope.launch {
                        drawerState.close()
                        onAttendanceRequestClick()
                    }
                },

                onLeaveHistoryClick = {
                    scope.launch {
                        drawerState.close()
                        onLeaveHistoryClick()
                    }
                },

                onAttendanceRequestHistoryClick = {
                    scope.launch {
                        drawerState.close()
                        onAttendanceRequestHistoryClick()
                    }
                },
                onCompOffApplicationClick = {
                    scope.launch {
                        drawerState.close()
                        onCompOffApplicationClick()
                    }
                },

                onLogoutClick = {
                    scope.launch {
                        drawerState.close()
                        showLogoutDialog = true
                    }

                }

            )

        }

    ) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        HomeTopBar(
            uiState = uiState,
            onMenuClick = {
                scope.launch {
                    drawerState.open()
                }
            }
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

        LeaveSummaryCard(
            remainingLeaves = uiState.remainingLeaves,
            leaveUiState = leaveUiState
        )



    }

        if (showLogoutDialog){
            LogoutDialog(
                onDismiss = {
                    showLogoutDialog = false
                },
                onConfirm = {
                    showLogoutDialog = false
                    onLogoutClick()
                }
            )
        }
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
