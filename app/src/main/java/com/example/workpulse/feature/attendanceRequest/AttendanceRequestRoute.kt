package com.example.workpulse.feature.attendanceRequest

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.workpulse.feature.attendanceRequest.components.AttendanceDatePickerDialog
import com.example.workpulse.feature.attendanceRequest.components.AttendanceRequestTypeDialog

@Composable
fun AttendanceRequestRoute(

    onBackClick: () -> Unit,
    onSaved: () -> Unit = {}

) {

    val viewModel: AttendanceRequestViewModel =
        hiltViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(uiState.successMessage) {

        uiState.successMessage?.let {

            Toast.makeText(
                context,
                it,
                Toast.LENGTH_SHORT
            ).show()

            viewModel.clearSuccessMessage()
            onSaved()

        }

    }

    LaunchedEffect(uiState.errorMessage) {

        uiState.errorMessage?.let {

            Toast.makeText(
                context,
                it,
                Toast.LENGTH_SHORT
            ).show()

            viewModel.clearErrorMessage()

        }

    }

    AttendanceRequestScreen(

        uiState = uiState,

        onBackClick = onBackClick,

        onFromDateClick =viewModel::showFromDatePicker,

        onToDateClick =viewModel :: showToDatePicker,

        onRequestTypeClick = viewModel :: showRequestTypeDialog,

        onExplanationChanged =
            viewModel::onExplanationChanged,

        onIncludeHolidaysChanged = viewModel :: onIncludeHolidaysChanged,

        onSubmitClick =
            viewModel::submitAttendanceRequest

    )


    if (uiState.showRequestTypeDialog) {

        AttendanceRequestTypeDialog(

            selectedType = uiState.requestType,

            onDismiss = viewModel::hideRequestTypeDialog,

            onSelected = {

                viewModel.onRequestTypeSelected(it)

                viewModel.hideRequestTypeDialog()

            }

        )

    }

    if (uiState.showFromDatePicker) {

        AttendanceDatePickerDialog (

            onDismiss = viewModel::hideFromDatePicker,

            onDateSelected = {

                viewModel.onFromDateSelected(it)

                viewModel.hideFromDatePicker()

            }

        )

    }

    if (uiState.showToDatePicker) {

        AttendanceDatePickerDialog (

            onDismiss = viewModel::hideToDatePicker,

            onDateSelected = {

                viewModel.onToDateSelected(it)

                viewModel.hideToDatePicker()

            }

        )

    }

}
