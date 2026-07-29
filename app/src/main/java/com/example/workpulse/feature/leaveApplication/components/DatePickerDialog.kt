package com.example.workpulse.feature.leaveApplication.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable

@Composable
fun DatePickerDialog(
    title: String,
    onDismiss: () -> Unit,
    onDateSelected: (Long) -> Unit
) {

    val datePickerState = rememberDatePickerState()

    DatePickerDialog(

        onDismissRequest = onDismiss,

        confirmButton = {

            TextButton(
                onClick = {

                    datePickerState.selectedDateMillis?.let {

                        onDateSelected(it)

                    }

                    onDismiss()

                }
            ) {

                Text("OK")

            }

        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Cancel")

            }

        }

    ) {

        DatePicker(
            state = datePickerState,
            title = {
                Text(title)
            }
        )

    }

}