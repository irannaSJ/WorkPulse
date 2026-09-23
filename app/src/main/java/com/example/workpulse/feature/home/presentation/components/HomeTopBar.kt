package com.example.workpulse.feature.home.presentation.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.workpulse.R
import com.example.workpulse.core.ui.theme.Dimens
import com.example.workpulse.core.ui.theme.AdaptiveLayout
import com.example.workpulse.feature.home.presentation.HomeUiState
import java.io.File
import java.util.Calendar

@Composable
fun HomeTopBar(

    uiState : HomeUiState,
    onMenuClick : () -> Unit
) {

    val greeting = remember {

        when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {

            in 0..11 -> "Good Morning"

            in 12..16 -> "Good Afternoon"

            else -> "Good Evening"

        }

    }

    BoxWithConstraints {
        val topSpacing = if (maxWidth >= AdaptiveLayout.MediumBreakpoint) Dimens.Space32 else Dimens.Space20
        Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(
                onClick = onMenuClick
            ) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(R.drawable.updated_workpulse_logo),
                contentDescription = null,
                modifier = Modifier.height(Dimens.HomeLogoHeight)
            )
        }

        Spacer(modifier = Modifier.height(topSpacing))

        Text(
            text = greeting,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(Dimens.Space4))

        Text(
            text = "Let's make today productive",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        }
    }

}
