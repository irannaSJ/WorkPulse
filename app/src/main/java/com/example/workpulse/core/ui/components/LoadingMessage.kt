package com.example.workpulse.core.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay

@Composable
fun LoadingMessage(

    message: String,

    modifier: Modifier = Modifier

) {

    var currentMessage by remember {

        mutableStateOf(message)

    }

    val alphaAnim = remember {

        Animatable(1f)

    }

    val offsetAnim = remember {

        Animatable(0f)

    }

    LaunchedEffect(message) {

        if (message == currentMessage)
            return@LaunchedEffect

        // Fade Out

        alphaAnim.animateTo(
            targetValue = 0f,
            animationSpec = tween(
                durationMillis = 220,
                easing = FastOutSlowInEasing
            )
        )

        offsetAnim.animateTo(
            targetValue = -10f,
            animationSpec = tween(220)
        )

        currentMessage = message

        offsetAnim.snapTo(10f)

        alphaAnim.snapTo(0f)

        // Fade In

        alphaAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 260,
                easing = FastOutSlowInEasing
            )
        )

        offsetAnim.animateTo(
            targetValue = 0f,
            animationSpec = tween(260)
        )

    }
    val loadingColor = if (isSystemInDarkTheme()) {

        Color.White.copy(alpha = .92f)

    } else {

        MaterialTheme.colorScheme
            .onBackground
            .copy(alpha = .70f)

    }

    Text(

        text = currentMessage,

        modifier = modifier.graphicsLayer {

            alpha= alphaAnim.value

            translationY = offsetAnim.value

        },

        textAlign = TextAlign.Center,

        style = MaterialTheme.typography.bodyMedium,

        color = loadingColor

    )

}