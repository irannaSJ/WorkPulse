package com.example.workpulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.workpulse.core.navigation.AppNavHost
import com.example.workpulse.core.ui.theme.WorkPulseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WorkPulseTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}

