package com.example.workpulse.feature.attendanceRequest.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DateRangeSection(

    fromDate: Long?,

    toDate: Long?,

    onFromDateClick: () -> Unit,

    onToDateClick: () -> Unit,

    modifier: Modifier = Modifier

) {

    Row(

        modifier = modifier,

        horizontalArrangement = Arrangement.spacedBy(12.dp)

    ) {

        RequestFieldButton(

            modifier = Modifier.weight(1f),

            label = "From Date",

            value = fromDate.toDisplayDate(),

            icon = Icons.Outlined.CalendarMonth,

            onClick = onFromDateClick

        )

        RequestFieldButton(

            modifier = Modifier.weight(1f),

            label = "To Date",

            value = toDate.toDisplayDate(),

            icon = Icons.Outlined.CalendarMonth,

            onClick = onToDateClick

        )

    }

}


private fun Long?.toDisplayDate(): String {

    if (this == null) {

        return "Select Date"

    }

    return Instant
        .ofEpochMilli(this)
        .atZone(ZoneId.systemDefault())
        .format(

            DateTimeFormatter.ofPattern(

                "dd MMM yyyy",

                Locale.getDefault()

            )

        )

}