//package com.example.workpulse.feature.login.presentation
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.scaleIn
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.navigationBarsPadding
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.statusBarsPadding
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import kotlinx.coroutines.delay
//
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.outlined.Email
//import androidx.compose.material.icons.outlined.Lock
//import androidx.compose.material.icons.outlined.Visibility
//import androidx.compose.material.icons.outlined.VisibilityOff
//
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.OutlinedTextFieldDefaults
//
//import androidx.compose.ui.text.input.PasswordVisualTransformation
//import androidx.compose.ui.text.input.VisualTransformation
//
//import com.example.workpulse.R
//
//
//@Composable
//fun LoginScreen(
//    modifier: Modifier = Modifier,
//    uiState: LoginUiState,
//    onEmailChanged: (String) -> Unit,
//    onPasswordChanged: (String) -> Unit,
//    onPasswordVisibility: () -> Unit,
//    onLoginClick: () -> Unit
//) {
//
//    var startAnimation by remember {
//        mutableStateOf(false)
//    }
//
//    LaunchedEffect(Unit) {
//        delay(150)
//        startAnimation = true
//    }
//
//    Surface(
//        modifier = modifier.fillMaxSize(),
//        color = Color(0xFFF8FAFC)
//    ) {
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(
//                    Brush.verticalGradient(
//                        listOf(
//                            Color.White,
//                            Color(0xFFF8FAFC)
//                        )
//                    )
//                )
//        ) {
//
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .verticalScroll(rememberScrollState())
//                    .statusBarsPadding()
//                    .navigationBarsPadding()
//                    .padding(horizontal = 24.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//
//                Spacer(modifier = Modifier.height(40.dp))
//
//                AnimatedVisibility(
//                    visible = startAnimation,
//                    enter = fadeIn() + scaleIn(
//                        initialScale = 0.85f
//                    )
//                ) {
//
//                    Image(
//                        painter = painterResource(id = R.drawable.workpulse_logo),
//                        contentDescription = "Logo",
//                        modifier = Modifier.height(150.dp),
//                        contentScale = ContentScale.Fit
//                    )
//
//                }
//
//                Spacer(modifier = Modifier.height(40.dp))
//
//                AnimatedVisibility(
//                    visible = startAnimation,
//                    enter = fadeIn()
//                ) {
//
//                    Column(
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//
//                        Text(
//                            text = "Welcome Back",
//                            fontSize = 30.sp,
//                            fontWeight = FontWeight.Bold,
//                            color = Color(0xFF111827)
//                        )
//
//                        Spacer(modifier = Modifier.height(10.dp))
//
//                        Text(
//                            text = "Sign in to your employee account",
//                            fontSize = 16.sp,
//                            color = Color(0xFF6B7280)
//                        )
//
//                    }
//
//                }
//
//                Spacer(modifier = Modifier.height(42.dp))
//
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .shadow(
//                            elevation = 18.dp,
//                            shape = RoundedCornerShape(28.dp),
//                            ambientColor = Color.Black.copy(alpha = 0.08f),
//                            spotColor = Color.Black.copy(alpha = 0.08f)
//                        ),
//                    shape = RoundedCornerShape(28.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = Color.White
//                    ),
//                    elevation = CardDefaults.cardElevation(
//                        defaultElevation = 0.dp
//                    )
//                ) {
//
//                    Column(
//                        modifier = Modifier.padding(24.dp),
//                        verticalArrangement = Arrangement.spacedBy(18.dp)
//                    ) {
//
//                        PremiumTextField(
//                            value = uiState.email,
//                            onValueChange = onEmailChanged,
//                            hint = "Employee Email"
//                        )
//
//                        PremiumPasswordField(
//                            value = uiState.password,
//                            onValueChange = onPasswordChanged,
//                            visible = uiState.isPasswordVisible,
//                            onVisibilityChanged = onPasswordVisibility
//                        )
//
//                        uiState.errorMessage?.let { error ->
//
//                            Text(
//                                text = error,
//                                color = MaterialTheme.colorScheme.error,
//                                style = MaterialTheme.typography.bodyMedium
//                            )
//
//                        }
//
//                        Spacer(modifier = Modifier.height(10.dp))
//
//                        PremiumButton(
//                            text = if (uiState.isLoading)
//                                "Signing In..."
//                            else
//                                "Login",
//
//                            enabled = !uiState.isLoading,
//
//                            onClick = onLoginClick
//                        )
//
//                    }
//
//                }
//
//                Spacer(modifier = Modifier.height(30.dp))
//
//                Text(
//                    text = "Version 1.0.0",
//                    color = Color.Gray,
//                    fontSize = 13.sp
//                )
//
//                Spacer(modifier = Modifier.height(20.dp))
//
//            }
//
//        }
//
//    }
//
//}
//
//@Composable
//fun PremiumTextField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    hint: String
//) {
//
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(62.dp),
//
//        singleLine = true,
//
//        shape = RoundedCornerShape(18.dp),
//
//        placeholder = {
//            Text(
//                text = hint,
//                color = Color(0xFF9CA3AF)
//            )
//        },
//
//        leadingIcon = {
//            Icon(
//                imageVector = Icons.Outlined.Email,
//                contentDescription = null,
//                tint = Color(0xFF2563EB)
//            )
//        },
//
//        colors = OutlinedTextFieldDefaults.colors(
//
//            focusedContainerColor = Color.White,
//            unfocusedContainerColor = Color.White,
//
//            focusedBorderColor = Color(0xFF2563EB),
//            unfocusedBorderColor = Color(0xFFE5E7EB),
//
//            cursorColor = Color(0xFF2563EB)
//
//        )
//    )
//
//}
//
//@Composable
//fun PremiumPasswordField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    visible: Boolean,
//    onVisibilityChanged: () -> Unit
//) {
//
//    OutlinedTextField(
//
//        value = value,
//
//        onValueChange = onValueChange,
//
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(62.dp),
//
//        singleLine = true,
//
//        shape = RoundedCornerShape(18.dp),
//
//        placeholder = {
//            Text(
//                text = "Password",
//                color = Color(0xFF9CA3AF)
//            )
//        },
//
//        leadingIcon = {
//            Icon(
//                imageVector = Icons.Outlined.Lock,
//                contentDescription = null,
//                tint = Color(0xFF2563EB)
//            )
//        },
//
//        trailingIcon = {
//
//            IconButton(
//                onClick = onVisibilityChanged
//            ) {
//
//                Icon(
//                    imageVector =
//                        if (visible)
//                            Icons.Outlined.Visibility
//                        else
//                            Icons.Outlined.VisibilityOff,
//
//                    contentDescription =
//                        if (visible)
//                            "Hide Password"
//                        else
//                            "Show Password",
//
//                    tint = Color.Gray
//                )
//
//            }
//
//        },
//
//        visualTransformation =
//            if (visible)
//                VisualTransformation.None
//            else
//                PasswordVisualTransformation(),
//
//        colors = OutlinedTextFieldDefaults.colors(
//
//            focusedContainerColor = Color.White,
//            unfocusedContainerColor = Color.White,
//
//            focusedBorderColor = Color(0xFF2563EB),
//            unfocusedBorderColor = Color(0xFFE5E7EB),
//
//            cursorColor = Color(0xFF2563EB)
//
//        )
//
//    )
//
//}
//
//@Composable
//fun PremiumButton(
//    text: String,
//    enabled: Boolean = true,
//    onClick: () -> Unit
//) {
//
//    Button(
//
//        onClick = onClick,
//
//        enabled = enabled,
//
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(58.dp),
//
//        shape = RoundedCornerShape(18.dp),
//
//        elevation = ButtonDefaults.buttonElevation(
//            defaultElevation = 8.dp,
//            pressedElevation = 2.dp
//        ),
//
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color(0xFF2563EB)
//        )
//
//    ) {
//
//        if (enabled) {
//
//            Text(
//                text = text,
//                fontSize = 18.sp,
//                fontWeight = FontWeight.SemiBold
//            )
//
//        } else {
//
//            CircularProgressIndicator(
//                modifier = Modifier.size(24.dp),
//                strokeWidth = 2.dp,
//                color = Color.White
//            )
//
//        }
//
//    }
//
//}



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