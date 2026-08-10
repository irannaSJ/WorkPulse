package com.example.workpulse.feature.leaveApplication


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.EventNote
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workpulse.core.ui.theme.WorkPulseTheme
import com.example.workpulse.feature.leaveApplication.components.BottomActionButtons
import com.example.workpulse.feature.leaveApplication.components.DatesAndReasonCard
import com.example.workpulse.feature.leaveApplication.components.LeaveApplicationTopBar
import com.example.workpulse.feature.leaveApplication.components.LeaveDetailsCard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LeaveApplicationScreen(
    uiState: LeaveApplicationUiState,
    snackbarHostState: SnackbarHostState,
    onBackClick : () -> Unit,
    onLeaveTypeClick : () -> Unit,
    onFromDateClick : () -> Unit,
    onToDateClick : () -> Unit,
    onReasonChange : (String) -> Unit,
    onResetClick : () -> Unit,
    onSaveClick : () -> Unit,
    modifier : Modifier = Modifier

) {

    Scaffold(
        modifier= modifier,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        topBar = {
            LeaveApplicationTopBar(
                onBackClick = onBackClick,
            )
        },

    ) {
        padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                LeaveDetailsCard(
                    uiState = uiState,
                    onLeaveTypeClick = onLeaveTypeClick
                )
            }

            item {
                DatesAndReasonCard(
                    uiState = uiState,

                    onFromDateClick = onFromDateClick,
                    onToDateClick = onToDateClick,
                    onReasonChange = onReasonChange
                )
            }


            item {

                BottomActionButtons(

                    onResetClick = onResetClick,

                    onSaveClick = onSaveClick,
                    isSubmitting = uiState.isSubmitting

                )

            }

            item {

                Spacer(
                    modifier = Modifier.height(50.dp)
                )

            }
            }
        }
    }








