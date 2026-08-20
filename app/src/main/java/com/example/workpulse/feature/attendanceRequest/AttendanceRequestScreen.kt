package com.example.workpulse.feature.attendanceRequest

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.workpulse.core.ui.components.WorkPulseTopBar
import com.example.workpulse.feature.attendanceRequest.components.AttendanceRequestCard

@Composable
fun AttendanceRequestScreen(

    uiState: AttendanceRequestUiState,

    onBackClick: () -> Unit,

    onFromDateClick: () -> Unit,

    onToDateClick: () -> Unit,

    onRequestTypeClick: () -> Unit,
    onIncludeHolidaysChanged: (Boolean) -> Unit,

    onExplanationChanged: (String) -> Unit,

    onSubmitClick: () -> Unit

) {

    Scaffold(

        topBar = {

            WorkPulseTopBar(

                title = "Attendance Request",

                onBackClick = onBackClick

            )

        }

    ) { paddingValues ->

        AttendanceRequestContent(

            paddingValues = paddingValues,

            uiState = uiState,

            onFromDateClick = onFromDateClick,

            onToDateClick = onToDateClick,

            onRequestTypeClick = onRequestTypeClick,

            onIncludeHolidaysChanged = onIncludeHolidaysChanged,

            onExplanationChanged = onExplanationChanged,

            onSubmitClick = onSubmitClick

        )

    }

}



@Composable
private fun AttendanceRequestContent(

    paddingValues: PaddingValues,

    uiState: AttendanceRequestUiState,

    onFromDateClick: () -> Unit,

    onToDateClick: () -> Unit,

    onRequestTypeClick: () -> Unit,
    onIncludeHolidaysChanged : (Boolean) -> Unit,

    onExplanationChanged: (String) -> Unit,

    onSubmitClick: () -> Unit

) {

    if (uiState.isLoading) {

        Column(

            modifier = Modifier.fillMaxSize(),

            horizontalAlignment = Alignment.CenterHorizontally,

            verticalArrangement = Arrangement.Center

        ) {

            CircularProgressIndicator()

        }

        return

    }

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),

        verticalArrangement = Arrangement.spacedBy(20.dp)

    ) {

        AttendanceRequestCard(

            uiState = uiState,

            onFromDateClick = onFromDateClick,

            onToDateClick = onToDateClick,

            onRequestTypeClick = onRequestTypeClick,
            onIncludeHolidaysChanged = onIncludeHolidaysChanged,

            onExplanationChanged = onExplanationChanged

        )

        Button(

            onClick = onSubmitClick,

            modifier = Modifier.fillMaxWidth(),

            enabled = !uiState.isSubmitting

        ) {

            if (uiState.isSubmitting) {

                CircularProgressIndicator()

            } else {
                Log.d("Testing submit button","SUbmit Request button clicked")

                Text(

                    text = "Submit Request",

                    style = MaterialTheme.typography.titleMedium

                )

            }

        }

    }

}
