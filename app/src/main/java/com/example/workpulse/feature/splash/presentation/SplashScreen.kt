package com.example.workpulse.feature.splash.presentation

import android.window.SplashScreen
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.remote.creation.dsl.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.workpulse.R
import com.example.workpulse.core.ui.components.SplashLoadingIndicator
import com.example.workpulse.core.ui.theme.AppGradients
import com.example.workpulse.core.ui.theme.Dimens
import com.example.workpulse.core.ui.theme.AdaptiveLayout
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.workpulse.core.ui.components.LoadingMessage
import com.example.workpulse.core.ui.theme.BrandBlue


@Composable
fun SplashScreen(
    uiState: SplashUiState,
    onAnimationFinished : () -> Unit
){
    val background = if(isSystemInDarkTheme()){
        AppGradients.SplashDark
    }else{
        AppGradients.SplashLight
    }


    val logoAlpha = remember {
        Animatable(0f)
    }
    val logoScale = remember {
        Animatable(0.72f)
    }

    val logoRotation = remember {
        Animatable(-4f)
    }

    val loadingAlpha = remember { Animatable(0f) }

    val loadingOffset = remember { Animatable(20f) }

    var showLoadingSection by remember {
        mutableStateOf(false)
    }


    LaunchedEffect(Unit) {

        delay(200)

        coroutineScope {

            launch {

                logoAlpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 1200,
                        easing = FastOutSlowInEasing
                    )
                )

            }

            launch {

                logoScale.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 1200,
                        easing = FastOutSlowInEasing
                    )
                )

            }

            launch {

                logoRotation.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 1200,
                        easing = FastOutSlowInEasing
                    )
                )

            }

            delay(550)

            launch {

                loadingAlpha.animateTo(
                    1f,
                    tween(500)
                )

            }

            launch {

                loadingOffset.animateTo(
                    0f,
                    tween(500)
                )

            }
            delay(250)

            showLoadingSection = true



        }

    }

    LaunchedEffect(uiState.destination) {

        uiState.destination?.let {

            delay(300)

            onAnimationFinished()

        }

    }

    Box(
        modifier = Modifier.fillMaxSize().background(background)
    ){
        SplashBackground()

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(R.drawable.workpulse_logo),
                contentDescription = null,
                modifier = Modifier.size(Dimens.SplashLogo)
                    .graphicsLayer {
                        alpha = logoAlpha.value
                        scaleX = logoScale.value
                        scaleY = logoScale.value
                    }
            )

            Spacer(modifier = Modifier.height(Dimens.Space4))

//            Text(
//                text = "WorkPulse",
//                style = MaterialTheme.typography.displaySmall,
//                color = MaterialTheme.colorScheme.onBackground
//            )
//
//            Spacer(
//                modifier = Modifier.height(Dimens.Space64)
//            )

//            SplashLoadingIndicator()
            Column(

                modifier = Modifier.graphicsLayer {

                    alpha = loadingAlpha.value

                    translationY = loadingOffset.value

                }

            ) {

                SplashLoadingIndicator()

            }

            Spacer(modifier = Modifier.height(Dimens.Space16))
            LoadingMessage(message = uiState.loadingMessage)




        }
    }
}


@Composable
fun SplashBackground(){
    Box(modifier = Modifier.fillMaxSize()){
        DecorativeCircle(
            size = AdaptiveLayout.SplashCircleMedium,
            alpha = 0.05f,
            alignment = Alignment.TopStart
        )

        DecorativeCircle(
            size = AdaptiveLayout.SplashCircleSmall,
            alpha = 0.04f,
            alignment = Alignment.CenterEnd
        )

        DecorativeCircle(
            size = AdaptiveLayout.SplashCircleLarge,
            alpha = 0.03f,
            alignment = Alignment.BottomStart
        )
    }
}

@Composable
private fun DecorativeCircle(
    size : androidx.compose.ui.unit.Dp,
    alpha : Float,
    alignment: Alignment
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = alignment
    ){
        val circleColor = if(isSystemInDarkTheme()){
            BrandBlue.copy(alpha = alpha * 3f)
        }else{
            BrandBlue.copy(alpha = alpha)
        }
        Box(
                modifier = Modifier.size(size).clip(CircleShape)
                .background(circleColor)
        )
    }
}
