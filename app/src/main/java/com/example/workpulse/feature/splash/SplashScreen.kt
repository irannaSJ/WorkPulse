//package com.example.workpulse.feature.splash
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.statusBarsPadding
//
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.height
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import com.example.workpulse.R
//
//@Composable
//fun SplashScreen(
//    uiState: SplashUiState,
//    modifier: Modifier = Modifier
//) {
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.background)
//            .statusBarsPadding()
//    ) {
//
//        Column(
//            modifier = Modifier.fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            Image(
//                painter = painterResource(id = R.drawable.workpulse_logo),
//                contentDescription = "Company Logo",
//                modifier = Modifier.size(140.dp)
//            )
//
//            Spacer(modifier = Modifier.height(10.dp))
//
//            SplashAnimation()
//
//            Spacer(modifier = Modifier.weight(1f))
//        }
//    }
//}



package com.example.workpulse.feature.splash

import android.R.attr.visible
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workpulse.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    uiState: SplashUiState,
    onAnimationFinished: () -> Unit,
    modifier: Modifier = Modifier
) {

    val logoScale = remember { Animatable(0.7f) }
    val logoAlpha = remember { Animatable(0f) }

    var showTitle by remember { mutableStateOf(false) }
    var showSubtitle by remember { mutableStateOf(false) }
    var showAnimation by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {

        logoAlpha.animateTo(
            1f,
            animationSpec = tween(700)
        )

        logoScale.animateTo(
            1f,
            animationSpec = tween(
                durationMillis = 700,
                easing = FastOutSlowInEasing
            )
        )

        showTitle = true

        delay(200)

        showSubtitle = true

        delay(300)

        showAnimation = true
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF07142E),
                        Color(0xFF0D47A1),
                        Color(0xFF1976D2)
                    )
                )
            )
            .statusBarsPadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(48.dp))

            Column(
                modifier = Modifier
                    .weight(4f)
                    .fillMaxWidth(),           // Important
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(R.drawable.workpulse_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(170.dp)
                        .scale(logoScale.value)
                        .alpha(logoAlpha.value)
                )

                Spacer(modifier = Modifier.height(24.dp))

                androidx.compose.animation.AnimatedVisibility(
                    visible = showTitle,
                    enter = fadeIn() +
                            slideInVertically(
                                initialOffsetY = { it / 2 }
                            )
                ) {

                    Text(
                        text = "WorkPulse",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                }

                Spacer(modifier = Modifier.height(10.dp))

                androidx.compose.animation.AnimatedVisibility(
                    visible = showSubtitle,
                    enter = fadeIn(
                        animationSpec = tween(700)
                    )
                ) {

                    Text(
                        text = "Smart Attendance & Workforce Management",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                }

            }

            Box(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                androidx.compose.animation.AnimatedVisibility(
                    visible = showAnimation,
                    enter = fadeIn()
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp),          // Fixed height prevents layout shifts
                        contentAlignment = Alignment.Center
                    ) {

                        SplashAnimation(
                            onFinished = onAnimationFinished
                        )

                    }

                }

            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                LoadingDots()

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Preparing your workspace...",
                    color = Color.White.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.bodyMedium
                )

            }

        }

    }

}

@Composable
private fun LoadingDots() {

    val transition =
        rememberInfiniteTransition(label = "loading")

    val dot1 by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(500),
            RepeatMode.Reverse
        ),
        label = ""
    )

    val dot2 by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(
                durationMillis = 500,
                delayMillis = 150
            ),
            RepeatMode.Reverse
        ),
        label = ""
    )

    val dot3 by transition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            tween(
                durationMillis = 500,
                delayMillis = 300
            ),
            RepeatMode.Reverse
        ),
        label = ""
    )

    Row {

        Dot(dot1)

        Spacer(modifier = Modifier.width(8.dp))

        Dot(dot2)

        Spacer(modifier = Modifier.width(8.dp))

        Dot(dot3)

    }

}

@Composable
private fun Dot(alpha: Float) {

    Box(
        modifier = Modifier
            .size(10.dp)
            .scale(alpha)
            .alpha(alpha)
            .background(
                Color.White,
                CircleShape
            )
    )

}