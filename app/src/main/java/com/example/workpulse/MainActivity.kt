package com.example.workpulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.example.workpulse.core.navigation.AppNavHost
import com.example.workpulse.core.ui.theme.WorkPulseTheme
import com.example.workpulse.feature.config.domain.ConfigurationManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var configurationManager : ConfigurationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val configuration by configurationManager.configuration.collectAsState()
            LaunchedEffect(Unit) {
                configurationManager.loadFromCache()
            }
            WorkPulseTheme(configuredTheme = configuration?.theme) {
                val navController = rememberNavController()
                AppNavHost(navController = navController,
                    configuration = configuration)
            }
        }
    }
}
