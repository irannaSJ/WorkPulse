package com.example.workpulse.feature.leave.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.workpulse.feature.leave.LeaveSummaryUiState

@Composable
fun LeaveSummaryTopBar(

    onBackClick: () -> Unit

) {

    Row(

        modifier = Modifier.fillMaxWidth(),

        verticalAlignment = Alignment.CenterVertically,

        horizontalArrangement = Arrangement.SpaceBetween

    ) {

        IconButton(
            onClick = onBackClick
        ) {

            Icon(

                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,

                contentDescription = null,

                tint = Color(0xFF0F172A)

            )

        }

        Text(

            text = "Leave Summary",

            fontWeight = FontWeight.Bold,

            fontSize = 28.sp,

            color = Color(0xFF0F172A)

        )

        Icon(

            imageVector = Icons.Outlined.CalendarMonth,

            contentDescription = null,

            tint = Color(0xFF64748B)

        )

    }

}