package com.example.workpulse.feature.leaveApplication


import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.workpulse.feature.leaveApplication.components.DatePickerDialog
import com.example.workpulse.feature.leaveApplication.components.LeaveSuggestionBottomSheet
import com.example.workpulse.feature.leaveApplication.components.LeaveTypeDialog

@Composable
fun LeaveApplicationRoute(onBackClick : () -> Unit){

    val viewModel : LeaveApplicationViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showLeaveTypeDialog by remember { mutableStateOf(false) }
    var showFromDatePicker by remember { mutableStateOf(false   ) }
    var showToDatePicker by remember { mutableStateOf(false ) }


    val snackbarHostState = remember {
        SnackbarHostState()
    }


    LaunchedEffect(uiState.successMessage) {

        uiState.successMessage?.let {

            snackbarHostState.showSnackbar(it)

            viewModel.onResetClicked()

            viewModel.clearSuccessMessage()

        }

    }

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearErrorMessage()
        }
    }



    LeaveApplicationScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onBackClick = onBackClick,
        onLeaveTypeClick ={
            showLeaveTypeDialog = true
        },
        onFromDateClick = {
            android.util.Log.d(
                "LeaveApplication",
                "From Date Clicked"
            )
            showFromDatePicker = true
        },
        onToDateClick = {
            showToDatePicker = true
        },
        onReasonChange = viewModel::onReasonChanged,
        onResetClick = viewModel::onResetClicked,
        onSaveClick = viewModel::onSaveClick
    )

    if (showLeaveTypeDialog){
        LeaveTypeDialog(
            onDismiss = {
                showLeaveTypeDialog = false
            },
            onLeaveTypeSelected = {
                leaveType -> viewModel.onLeaveTypeSelected(leaveType)
                showLeaveTypeDialog = false
            }
        )
    }

    if(showFromDatePicker){
        DatePickerDialog(
            title = "Select From Date",
            onDismiss = {
                showFromDatePicker = false
            },
            onDateSelected = { selectedDate ->

                viewModel.onFromDateSelected(selectedDate)

            }
        )
    }

    if (showToDatePicker){
        DatePickerDialog(
            title = "Select To Date",
            onDismiss = {
                showToDatePicker = false
            },
            onDateSelected = {selectedDate ->
                viewModel.onToDateSelected(selectedDate)
            }
        )
    }

    if (uiState.showSuggestionDialog) {

        LeaveSuggestionBottomSheet(

            selectedLeaveType = uiState.leaveType
                ?: return@LeaveApplicationRoute,

            requestedDays = uiState.requestedDays,

            availableBalance = uiState.availableBalance ?: 0.0,

            suggestions = uiState.suggestedLeaveTypes,

            onDismiss = {

                viewModel.hideSuggestionDialog()

            },

            onSuggestionClick = { leaveType ->

                viewModel.onLeaveTypeSelected(leaveType)

                viewModel.hideSuggestionDialog()

            }

        )

    }

}