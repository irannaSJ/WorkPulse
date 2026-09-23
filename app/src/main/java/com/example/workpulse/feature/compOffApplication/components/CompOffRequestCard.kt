package com.example.workpulse.feature.compOffApplication.components

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workpulse.feature.attendanceRequest.components.DateRangeSection
import com.example.workpulse.feature.attendanceRequest.components.EmployeeField
import com.example.workpulse.feature.attendanceRequest.components.ExplanationField
import com.example.workpulse.feature.attendanceRequest.components.RequestTypeDropDown
import com.example.workpulse.feature.compOffApplication.CompOffApplicationUiState
import com.example.workpulse.core.ui.theme.Dimens
import com.example.workpulse.core.ui.theme.WorkPulseShapes

@Composable
fun CompOffRequestCard(
    uiState: CompOffApplicationUiState,
    onFromDateClick : () -> Unit,
    onToDateClick : () -> Unit,
    onReasonChanged : (String) -> Unit,
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
            EmployeeField(employeeName = uiState.employeeName)
            DateRangeSection(
                fromDate = uiState.fromDate,
                toDate = uiState.toDate,
                onFromDateClick = onFromDateClick,
                onToDateClick = onToDateClick
            )
            ExplanationField(
                value = uiState.reason,
                onValueChange = onReasonChanged
            )
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//fun previewCompOffRequestCard(){
//    CompOffRequestCard(
//        uiState = CompOffApplicationUiState(),
//        onFromDateClick = {},
//        onToDateClick = {},
//        onReasonChanged = {},
//    )
//}
