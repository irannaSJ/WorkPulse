package com.example.workpulse.feature.attendanceRequest.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun IncludeHolidaysSwitch(

    checked: Boolean,

    onCheckedChange: (Boolean) -> Unit,

    modifier: Modifier = Modifier

) {

    Row(

        modifier = modifier.fillMaxWidth(),

        verticalAlignment = Alignment.CenterVertically,

        horizontalArrangement = Arrangement.SpaceBetween

    ) {

        Text(

            text = "Include Holidays",

            style = MaterialTheme.typography.titleMedium

        )

        Switch(

            checked = checked,

            onCheckedChange = onCheckedChange

        )

    }

}