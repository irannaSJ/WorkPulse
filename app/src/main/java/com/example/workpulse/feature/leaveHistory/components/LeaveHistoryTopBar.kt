package com.example.workpulse.feature.leaveHistory.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.workpulse.core.ui.theme.WorkPulseTheme
import com.example.workpulse.data.local.entity.LeaveApplicationStatus
import com.example.workpulse.feature.leaveHistory.LeaveHistoryCardUi
import com.example.workpulse.feature.leaveHistory.LeaveHistoryScreen
import com.example.workpulse.feature.leaveHistory.LeaveSummaryUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveHistoryTopBar(

    onBackClick: () -> Unit

) {

    CenterAlignedTopAppBar(

        title = {

            Text(

                text = "Leave History",

                style = MaterialTheme.typography.titleLarge,

                fontWeight = FontWeight.Bold

            )

        },

        navigationIcon = {

            IconButton(

                onClick = onBackClick

            ) {

                Icon(

                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,

                    contentDescription = "Back"

                )

            }

        }

    )

}




