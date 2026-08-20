package com.example.workpulse.feature.compOffApplicationsHistory.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workpulse.core.ui.components.StatusChip
import com.example.workpulse.feature.compOffApplicationsHistory.CompOffDetailsUi
import com.example.workpulse.feature.leaveHistory.components.DetailRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompOffDetailsBottomSheet(
    leave : CompOffDetailsUi,
    onDismiss: () -> Unit
){
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {

        Column(

            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(
                    horizontal = 20.dp,
                    vertical = 8.dp
                ),

            verticalArrangement = Arrangement.spacedBy(16.dp)

        ){
            Text(
                text = "Compensatory Off Details",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            CompOffStatusChip(
                status = leave.status
            )
            Divider()

            DetailRow(
                title = "Employee",
                value = leave.employeeName
            )
            DetailRow(
                title = "From Date",
                value = leave.fromDate
            )
            DetailRow(
                title = "To Date",
                value = leave.toDate
            )
            DetailRow(
                title = "Reason",
                value = leave.reason
            )
            DetailRow(
                title = "Applied On",
                value = leave.appliedOn
            )
            DetailRow(
                title = "ERPNext ID",
                value = leave.erpNextId?:"-"
            )
            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Close"
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )
        }
    }
}