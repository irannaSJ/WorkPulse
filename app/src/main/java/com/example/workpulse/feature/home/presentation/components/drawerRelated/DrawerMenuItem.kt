package com.example.workpulse.feature.home.presentation.components.drawerRelated

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DrawerMenuItem(

    title: String,

    icon: ImageVector,

    onClick: () -> Unit,

    modifier: Modifier = Modifier,

    selected: Boolean = false

) {

    Row(

        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (selected)
                    MaterialTheme.colorScheme.primaryContainer
                else
                    MaterialTheme.colorScheme.surface
            )
            .clickable(
                onClick = onClick
            )
            .padding(horizontal = 18.dp),

        verticalAlignment = Alignment.CenterVertically,

        horizontalArrangement = Arrangement.Start

    ) {

        Icon(

            imageVector = icon,

            contentDescription = title,

            modifier = Modifier.size(22.dp),

            tint =
                if (selected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant

        )

        Spacer(
            modifier = Modifier.width(28.dp)
        )

        Text(

            text = title,

            style = MaterialTheme.typography.titleMedium,

            fontWeight =
                if (selected)
                    FontWeight.SemiBold
                else
                    FontWeight.Medium,

            color =
                if (selected)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface

        )

    }

}