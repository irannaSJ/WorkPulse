package com.example.workpulse.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.rememberScrollState

import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.workpulse.feature.home.HomeViewModel
import com.example.workpulse.feature.home.presentation.components.drawerRelated.LogoutDialog
import com.example.workpulse.feature.home.presentation.components.drawerRelated.NavigationDrawerContent
import com.example.workpulse.feature.leave.LeaveSummaryUiState
import com.example.workpulse.core.ui.theme.AdaptiveLayout
import com.example.workpulse.core.ui.theme.Dimens
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
    leaveUiState : LeaveSummaryUiState,
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

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val isWide = maxWidth >= AdaptiveLayout.MediumBreakpoint
        val horizontalPadding = if (isWide) Dimens.Space24 else Dimens.Space16

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding, vertical = Dimens.Space16)
                .widthIn(max = AdaptiveLayout.HomeContentMaxWidth)
                .verticalScroll(rememberScrollState())
        ) {

        HomeTopBar(
            uiState = uiState,
            onMenuClick = {
                scope.launch {
                    drawerState.open()
                }
            }
        )

        Spacer(modifier = Modifier.height(Dimens.Space20))

        DateTimeCard()

        Spacer(modifier = Modifier.height(Dimens.Space20))

        if (isWide) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(Dimens.Space20)
            ) {
                AttendanceCard(
                    modifier = Modifier.weight(1f).widthIn(max = AdaptiveLayout.HomeColumnMaxWidth),
                    attendanceState = uiState.attendanceState,
                    workingSeconds = uiState.workingSeconds,
                    punchInTime = uiState.punchInTime,
                    punchOutTime = uiState.punchOutTime,
                    onAttendanceClick = onAttendanceClick
                )
                LeaveSummaryCard(
                    modifier = Modifier.weight(1f).widthIn(max = AdaptiveLayout.HomeColumnMaxWidth),
                    remainingLeaves = uiState.remainingLeaves,
                    leaveUiState = leaveUiState
                )
            }
        } else {
            AttendanceCard(
                attendanceState = uiState.attendanceState,
                workingSeconds = uiState.workingSeconds,
                punchInTime = uiState.punchInTime,
                punchOutTime = uiState.punchOutTime,
                onAttendanceClick = onAttendanceClick
            )
            Spacer(modifier = Modifier.height(Dimens.Space20))
            LeaveSummaryCard(
                remainingLeaves = uiState.remainingLeaves,
                leaveUiState = leaveUiState
            )
        }



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
