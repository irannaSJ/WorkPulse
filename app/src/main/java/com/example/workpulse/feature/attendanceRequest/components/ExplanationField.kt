package com.example.workpulse.feature.attendanceRequest.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ExplanationField(

    value: String,

    onValueChange: (String) -> Unit,

    modifier: Modifier = Modifier

) {

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Text(
            text = "Explanation",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )

        OutlinedTextField(

            value = value,

            onValueChange = {

                if (it.length <= 500) {

                    onValueChange(it)

                }

            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .heightIn(min = 140.dp),

            placeholder = {

                Text(
                    text = "Explain why you are requesting On Duty or Work From Home..."
                )

            },

            supportingText = {

                Text(
                    text = "${value.length}/500"
                )

            },

            minLines = 5,

            maxLines = 8

        )

    }

}