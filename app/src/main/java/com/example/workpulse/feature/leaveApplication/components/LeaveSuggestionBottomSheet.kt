package com.example.workpulse.feature.leaveApplication.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.workpulse.feature.leaveApplication.LeaveSuggestionUi
import com.example.workpulse.feature.leaveApplication.LeaveType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveSuggestionBottomSheet(

    selectedLeaveType: LeaveType,

    requestedDays: Int,

    availableBalance: Double,

    suggestions: List<LeaveSuggestionUi>,

    onDismiss: () -> Unit,

    onSuggestionClick: (LeaveType) -> Unit

){

    ModalBottomSheet(

        onDismissRequest = onDismiss,

        shape = RoundedCornerShape(
            topStart = 28.dp,
            topEnd = 28.dp
        ),

        dragHandle = {

            BottomSheetDefaults.DragHandle()

        }

    ) {

        Text(

            text = "Insufficient Leave Balance",

            style = MaterialTheme.typography.headlineSmall,

            fontWeight = FontWeight.Bold

        )


        Text(

            text =
                "You requested $requestedDays day(s) of " +
                        "${selectedLeaveType.displayName}.",

            style = MaterialTheme.typography.bodyMedium,

            color = MaterialTheme.colorScheme.onSurfaceVariant

        )


        Spacer(modifier = Modifier.height(8.dp))

        Text(

            text =
                "Available Balance : $availableBalance day(s)",

            style = MaterialTheme.typography.titleMedium,

            color = MaterialTheme.colorScheme.primary,

            fontWeight = FontWeight.SemiBold

        )



        Spacer(modifier = Modifier.height(28.dp))

        Text(

            text = "Suggested Leave Types",

            style = MaterialTheme.typography.titleMedium,

            fontWeight = FontWeight.Bold

        )


        Column(

            verticalArrangement = Arrangement.spacedBy(12.dp)

        ) {

            suggestions.forEach {

                LeaveSuggestionItem(

                    suggestion = it,

                    onClick = onSuggestionClick

                )

            }

        }


        Spacer(modifier = Modifier.height(28.dp))

        OutlinedButton(

            onClick = onDismiss,

            modifier = Modifier.fillMaxWidth(),

            shape = RoundedCornerShape(16.dp)

        ) {

            Text("Cancel")

        }

        Spacer(

            modifier = Modifier.navigationBarsPadding()

        )

    }

}

//@Preview(showBackground = true)
//@Composable
//private fun LeaveSuggestionBottomSheetPreview() {
//
//    LeaveSuggestionBottomSheet(
//
//        selectedLeaveType = LeaveType.CASUAL,
//
//        requestedDays = 5,
//
//        availableBalance = 2.0,
//
//        suggestions = listOf(
//
//            LeaveSuggestionUi(
//                LeaveType.PRIVILEGE,
//                12.0
//            ),
//
//            LeaveSuggestionUi(
//                LeaveType.SICK,
//                8.0
//            ),
//
//            LeaveSuggestionUi(
//                LeaveType.COMP_OFF,
//                5.0
//            )
//
//        ),
//
//        onDismiss = {},
//
//        onSuggestionClick = {}
//
//    )
//
//}