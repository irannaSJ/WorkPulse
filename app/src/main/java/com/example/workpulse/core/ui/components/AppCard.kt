package com.example.workpulse.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {

    val isDark = isSystemInDarkTheme()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),

        colors = CardDefaults.cardColors(
            containerColor = if (isDark) {
                Color(0xFF18294A).copy(alpha = 0.95f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),

        border = if (isDark) {
            BorderStroke(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.08f)
            )
        } else {
            null
        },

        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isDark) 12.dp else 8.dp
        )

    ) {

        Column(
            modifier = Modifier.padding(24.dp),
            content = content
        )

    }

}