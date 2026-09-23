package com.example.workpulse.feature.attendance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workpulse.core.ui.theme.Dimens
import com.example.workpulse.core.ui.theme.AdaptiveLayout
import com.example.workpulse.core.ui.theme.WorkPulseShapes
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun DateTimeCard(
    modifier: Modifier = Modifier
) {

    var currentTime by remember {
        mutableStateOf(getCurrentTime())
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = getCurrentTime()
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = WorkPulseShapes.large,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val contentModifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.Space20, vertical = Dimens.Space16)
            if (maxWidth < AdaptiveLayout.DateTimeStackBreakpoint) {
                Column(
                    modifier = contentModifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Dimens.Space12)
                ) {
                    DateContent()
                    TimeContent(currentTime)
                }
            } else Row(
                modifier = contentModifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(Dimens.Icon28)
                )

                Spacer(modifier = Modifier.width(Dimens.Space8))

                Text(
                    text = getCurrentDate(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Divider(
                modifier = Modifier
                    .height(Dimens.Space32)
                    .width(Dimens.DividerThickness),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Outlined.AccessTime,
                    contentDescription = null,
                    tint = Color(0xFF22C55E),
                    modifier = Modifier.size(Dimens.Icon28)
                )

                Spacer(modifier = Modifier.width(Dimens.Space8))

                Text(
                    text = currentTime,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            }
        }

    }

}

@Composable
private fun DateContent() = Row(verticalAlignment = Alignment.CenterVertically) {
    Icon(Icons.Outlined.CalendarMonth, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(Dimens.Icon28))
    Spacer(modifier = Modifier.width(Dimens.Space8))
    Text(getCurrentDate(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
}

@Composable
private fun TimeContent(currentTime: String) = Row(verticalAlignment = Alignment.CenterVertically) {
    Icon(Icons.Outlined.AccessTime, null, tint = Color(0xFF22C55E), modifier = Modifier.size(Dimens.Icon28))
    Spacer(modifier = Modifier.width(Dimens.Space8))
    Text(currentTime, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
}


private fun getCurrentDate(): String {

    return SimpleDateFormat(
        "EEEE, dd MMM yyyy",
        Locale.getDefault()
    ).format(Date())

}

private fun getCurrentTime(): String {
    return SimpleDateFormat(
        "hh:mm a",
        Locale.getDefault()
    ).format(Date())

}
