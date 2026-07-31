package com.example.workpulse.feature.home.presentation.components.drawerRelated

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun LogoutDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {
            Text(
                text = "Logout",
                style = MaterialTheme.typography.titleLarge
            )
        },

        text = {
            Text(
                text = "Are you sure you want to logout from WorkPulse?",
                style = MaterialTheme.typography.bodyMedium
            )
        },

        confirmButton = {

            TextButton(
                onClick = onConfirm
            ) {

                Text("Logout")

            }

        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Cancel")

            }

        }

    )

}