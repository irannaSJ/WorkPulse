package com.example.workpulse.feature.compOffApplication


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.workpulse.feature.leaveApplication.components.DatePickerDialog
import kotlinx.coroutines.delay


@Composable
fun CompOffApplicationRoute(onBackClick : () -> Unit) {
    val viewModel : CompOffApplicationViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showFromDatePicker by remember { mutableStateOf(false  ) }
    var showToDatePicker by remember { mutableStateOf(false) }



    LaunchedEffect(uiState.successMessage) {

        if (uiState.successMessage != null) {

            delay(3000)

            viewModel.clearSuccessMessage()
        }
    }

    LaunchedEffect(uiState.errorMessage) {
        if(uiState.errorMessage != null){
            delay(3000)
            viewModel.clearErrorMessage()
        }
    }

    CompOffApplicationScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onFromDateClick = { showFromDatePicker = true },
        onToDateClick = {showToDatePicker = true},
        onReasonChanged = viewModel::onReasonChanged,
        onSubmitClick = viewModel::onSaveClick
    )

    if (showFromDatePicker){
        DatePickerDialog(
            title = "Select From Date",
            onDismiss = {
                showFromDatePicker = false
            },
            onDateSelected = {selectedDate -> viewModel.onFromDateSelected(selectedDate)}
        )
        }

    if(showToDatePicker)
    {
        DatePickerDialog(
            title = "Select To Date",
            onDismiss = {
                showToDatePicker = false
            },
            onDateSelected = {
                selectedDate -> viewModel.onToDateSelected(selectedDate)
            }
        )
    }




}