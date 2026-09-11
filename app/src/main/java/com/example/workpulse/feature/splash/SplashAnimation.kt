//package com.example.workpulse.feature.splash
//
//import androidx.compose.animation.Crossfade
//import androidx.compose.animation.core.tween
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.size
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import kotlinx.coroutines.delay
//import com.example.workpulse.R
//
//@Composable
//fun SplashAnimation() {
//    // 1. Reference your uploaded PNGs (assuming they are in res/drawable)
//    val images = listOf(
//        R.drawable.clocklogo,
//        R.drawable.employeelogo,
//        R.drawable.growthlogo,
//        R.drawable.checkmarklogo
//    )
//
//    var currentIndex by remember { mutableStateOf(0) }
//
//    // 2. Create the infinite loop coroutine
//    LaunchedEffect(Unit) {
//        while (true) {
//            delay(600) // How long each image stays fully visible (in milliseconds)
//            currentIndex = (currentIndex + 1) % images.size
//        }
//    }
//
//    // 3. Render the UI with a Crossfade transition
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Crossfade(
//            targetState = images[currentIndex],
//            animationSpec = tween(durationMillis = 300), // Speed of the morph/fade
//            label = "splash_crossfade"
//        ) { imageRes ->
//            Image(
//                painter = painterResource(id = imageRes),
//                contentDescription = "App Loading",
//                modifier = Modifier.size(120.dp) // Adjust size as needed
//            )
//        }
//    }
//}

package com.example.workpulse.feature.splash

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.workpulse.R
import kotlinx.coroutines.delay
import com.example.workpulse.core.ui.theme.AdaptiveLayout
import com.example.workpulse.core.ui.theme.Dimens
import com.example.workpulse.core.ui.theme.WorkPulseShapes

private data class SplashFeature(
    val icon: Int,
    val title: String,
    val subtitle: String
)

@Composable
fun SplashAnimation(onFinished : () -> Unit) {

    val features = remember {

        listOf(

            SplashFeature(
                R.drawable.clocklogo,
                "Attendance",
                "Tracking your attendance"
            ),

            SplashFeature(
                R.drawable.employeelogo,
                "Employees",
                "Loading employee information"
            ),

            SplashFeature(
                R.drawable.growthlogo,
                "Insights",
                "Preparing your dashboard"
            ),

            SplashFeature(
                R.drawable.checkmarklogo,
                "Ready",
                "Everything is ready"
            )
        )
    }

    var currentIndex by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(Unit) {

        for (i in features.indices) {

            currentIndex = i

            delay(1000)
        }

        delay(400)

        onFinished()
    }

    AnimatedContent(
        modifier = Modifier.fillMaxWidth(),
        targetState = features[currentIndex],
        transitionSpec = {
            (
                    slideInVertically(
                        animationSpec = tween(500),
                        initialOffsetY = { it / 2 }
                    ) +
                            fadeIn(
                                animationSpec = tween(500)
                            ) +
                            scaleIn(
                                initialScale = 0.85f
                            )
                    )
                .togetherWith(
                    slideOutVertically(
                        animationSpec = tween(500),
                        targetOffsetY = { -it / 2 }
                    ) +
                            fadeOut(
                                animationSpec = tween(500)
                            ) +
                            scaleOut(
                                targetScale = 1.15f
                            )
                )
                .using(
                    SizeTransform(
                        clip = false
                    )
                )
        },
        contentAlignment = Alignment.Center,      // <-- Add this
        label = "Splash Animation"
    ) { feature ->

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = AdaptiveLayout.SplashFeatureMaxWidth),
            shape = WorkPulseShapes.large,
            colors = CardDefaults.cardColors(
                containerColor = Color.White.copy(alpha = 0.15f)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 24.dp,
                        vertical = 28.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {

                Image(
                    painter = painterResource(feature.icon),
                    contentDescription = feature.title,
                    modifier = Modifier.size(AdaptiveLayout.SplashFeatureIcon)
                )

                Text(
                    text = feature.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = feature.subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

}
