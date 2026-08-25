package com.example.workpulse.feature.login.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.workpulse.R
import com.example.workpulse.core.ui.components.AppCard
import com.example.workpulse.core.ui.components.AppTextField
import com.example.workpulse.core.ui.components.PasswordTextField
import com.example.workpulse.core.ui.components.PrimaryButton
import com.example.workpulse.core.ui.theme.AppGradients
import com.example.workpulse.core.ui.theme.Dimens


import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(

    uiState: LoginUiState,

    onEmailChanged: (String) -> Unit,

    onPasswordChanged: (String) -> Unit,

    onLoginClick: () -> Unit

) {

    val background = if (isSystemInDarkTheme()) {
        AppGradients.SplashDark
    } else {
        AppGradients.SplashLight
    }

    val logoAlpha = remember { Animatable(0f) }
    val logoScale = remember { Animatable(0.94f) }

    val cardAlpha = remember { Animatable(0f) }
    val cardOffset = remember { Animatable(24f) }

    val titleAlpha = remember { Animatable(0f) }

    val emailAlpha = remember { Animatable(0f) }

    val passwordAlpha = remember { Animatable(0f) }

    val buttonAlpha = remember { Animatable(0f) }



    LaunchedEffect(Unit) {

        delay(150)

        coroutineScope {

            launch {
                logoAlpha.animateTo(
                    1f,
                    tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                )
            }

            launch {
                logoScale.animateTo(
                    1f,
                    tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                )
            }

        }

        delay(150)

        coroutineScope {

            launch {
                cardAlpha.animateTo(
                    1f,
                    tween(
                        durationMillis = 600,
                        easing = FastOutSlowInEasing
                    )
                )
            }

            launch {
                cardOffset.animateTo(
                    0f,
                    tween(
                        durationMillis = 600,
                        easing = FastOutSlowInEasing
                    )
                )
            }

        }

        delay(150)

        titleAlpha.animateTo(
            1f,
            tween(300)
        )

        delay(80)

        emailAlpha.animateTo(
            1f,
            tween(300)
        )

        delay(80)

        passwordAlpha.animateTo(
            1f,
            tween(300)
        )

        delay(80)

        buttonAlpha.animateTo(
            1f,
            tween(300)
        )

    }





    Column(

        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = Dimens.Space24)
            .padding(top = 48.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Top

    ) {

        Image(

            painter = painterResource(R.drawable.workpulse_logo),

            contentDescription = null,

            modifier = Modifier
                .size(Dimens.SplashLogo)
                .graphicsLayer {

                    alpha = logoAlpha.value

                    scaleX = logoScale.value

                    scaleY = logoScale.value

                }

        )

        Spacer(
            modifier = Modifier.height(Dimens.Space24)
        )

        AppCard(

            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp)
                .graphicsLayer {

                    alpha = cardAlpha.value

                    translationY = cardOffset.value

                }

        ) {

            Text(

                text = "Welcome Back",

                style = MaterialTheme.typography.headlineMedium,

                fontWeight = FontWeight.Bold,

            )

            Spacer(
                modifier = Modifier.height(Dimens.Space8)
            )

            Text(

                text = "Sign in to continue",

                modifier = Modifier.graphicsLayer {

                    alpha = titleAlpha.value

                },

                style = MaterialTheme.typography.bodyMedium,

                color = MaterialTheme.colorScheme.onSurfaceVariant

            )

            Spacer(
                modifier = Modifier.height(Dimens.Space24)
            )

            AppTextField(

                value = uiState.email,

                onValueChange = onEmailChanged,

                label = "Email",

                placeholder = "Enter your email",

                modifier = Modifier.graphicsLayer {

                    alpha = emailAlpha.value

                },

                leadingIcon = Icons.Outlined.Email

            )

            Spacer(
                modifier = Modifier.height(Dimens.Space16)
            )

            PasswordTextField(

                value = uiState.password,
                modifier = Modifier.graphicsLayer {

                    alpha = passwordAlpha.value

                },

                onValueChange = onPasswordChanged

            )

            Spacer(
                modifier = Modifier.height(Dimens.Space32)
            )

            PrimaryButton(

                text = "Login",

                onClick = onLoginClick,

                modifier = Modifier.fillMaxWidth()
                    .graphicsLayer{
                        alpha = buttonAlpha.value
                    }

            )

        }

        Spacer(modifier = Modifier.height(Dimens.Space32))

        Text(
            text = "WorkPulse v1.0.0",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

    }

}