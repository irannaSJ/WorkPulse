package com.example.workpulse.feature.attendanceRequest.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.workpulse.feature.attendanceRequest.AttendanceRequestType
import com.example.workpulse.feature.attendanceRequest.AttendanceRequestUiState
import com.example.workpulse.core.ui.theme.Dimens
import com.example.workpulse.core.ui.theme.WorkPulseShapes

@Composable
fun AttendanceRequestCard(
    uiState: AttendanceRequestUiState,
    onFromDateClick: () -> Unit,
    onToDateClick: () -> Unit,
    onRequestTypeClick: () -> Unit,
    onIncludeHolidaysChanged: (Boolean) -> Unit,
    onExplanationChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    ElevatedCard(

        modifier = modifier.fillMaxWidth(),
        shape = WorkPulseShapes.large,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.Space20),
            verticalArrangement = Arrangement.spacedBy(Dimens.Space20)
        ) {
            EmployeeField(
                employeeName = uiState.employeeName
            )
            DateRangeSection(
                fromDate = uiState.fromDate,
                toDate = uiState.toDate,
                onFromDateClick = onFromDateClick,
                onToDateClick = onToDateClick
            )

            RequestTypeDropDown(
                requestType = uiState.requestType,
                onClick = onRequestTypeClick
            )

            IncludeHolidaysSwitch(
                checked = uiState.includeHolidays,
                onCheckedChange = onIncludeHolidaysChanged
            )
            ExplanationField(
                value = uiState.explanation,
                onValueChange = onExplanationChanged
            )

        }

    }

}
