package com.example.workpulse.feature.compOffApplicationsHistory.components

import android.R.attr.fontWeight
import android.R.attr.text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompOffHistoryTopBar(onBackClick : () -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            Text (
                text = "Compensatory Off History",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}