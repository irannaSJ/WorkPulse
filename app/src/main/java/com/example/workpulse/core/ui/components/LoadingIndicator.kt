package com.example.workpulse.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.example.workpulse.core.ui.theme.AppGradients
import com.example.workpulse.core.ui.theme.Dimens



import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Brush
import com.example.workpulse.core.ui.theme.BrandGold
import com.example.workpulse.core.ui.theme.BrandPurple

@Composable
fun WorkPulseLoadingIndicator(
    modifier: Modifier = Modifier,
    text: String? = null
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = Dimens.Space4
        )

        if (text != null) {

            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

        }
    }
}


@Composable
fun SplashLoadingIndicator(
    modifier: Modifier = Modifier
) {

    val infiniteTransition = rememberInfiniteTransition(
        label = "SplashLoading"
    )

    val offset by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1400,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "Offset"
    )

    val shimmerBrush = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
            MaterialTheme.colorScheme.primary,
            BrandPurple,
            BrandGold,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
        ),
        startX = offset * 400f,
        endX = offset * 400f + 300f
    )

    Box(
        modifier = modifier
            .width(Dimens.ProgressWidth)
            .height(Dimens.ProgressHeight)
            .clip(CircleShape)
            .background(
                MaterialTheme.colorScheme.surface.copy(alpha = 0.35f)
            )
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(shimmerBrush)
        )

    }
}