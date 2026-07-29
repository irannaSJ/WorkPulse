package com.example.workpulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.workpulse.core.navigation.AppNavHost
import com.example.workpulse.feature.home.presentation.HomeScreen
import com.example.workpulse.feature.login.LoginRoute
import com.example.workpulse.feature.login.LoginViewModel
import com.example.workpulse.feature.login.presentation.LoginScreen
import com.example.workpulse.feature.splash.SplashAnimation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AppNavHost(navController = navController)
//            LoginScreen()
//            LoginRoute(viewModel = LoginViewModel)
//            HomeScreen()
        }
    }
}

