package com.example.workpulse.feature.leaveApplication.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.workpulse.feature.leaveApplication.LeaveType


@Composable
fun LeaveTypeDialog(
    onDismiss: () -> Unit,
    onLeaveTypeSelected: (LeaveType) -> Unit
) {

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {
            Text("Select Leave Type")
        },

        text = {

            androidx.compose.foundation.layout.Column {

                LeaveType.entries.forEach { leaveType ->

                    Text(
                        text = leaveType.displayName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                                onLeaveTypeSelected(leaveType)

                            }
                            .padding(vertical = 16.dp)
                    )

                }

            }

        },

        confirmButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Close")

            }

        }

    )

}