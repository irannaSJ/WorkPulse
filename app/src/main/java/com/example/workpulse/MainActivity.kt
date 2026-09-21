package com.example.workpulse

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.compose.rememberNavController
import com.example.workpulse.core.navigation.AppNavHost
import com.example.workpulse.core.ui.theme.WorkPulseTheme
import com.example.workpulse.feature.config.domain.ConfigurationManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.CancellationException

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var configurationManager : ConfigurationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val scope = rememberCoroutineScope()
            val configuration by configurationManager.configuration.collectAsState()
            LaunchedEffect(Unit) {
                configurationManager.loadFromCache()
                try {
                    configurationManager.sync()
                }catch (e : CancellationException){
                    throw e
                }catch (e: Exception){
                    Log.w("WorkPulseConfig", "Configuration sync failed. using cached configuration.",e)
                }

            }
            LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {

                scope.launch{
                    try {
                        configurationManager.sync()
                    }catch (e: CancellationException){
                        throw e
                    }catch (e: Exception){
                        Log.w("WorkPulseConfig", "Configuration refresh failed. Keeping cached configuration.", e)
                    }
                }
            }


            WorkPulseTheme(configuredTheme = configuration?.theme) {
                val navController = rememberNavController()
                AppNavHost(navController = navController,
                    configuration = configuration)
            }
        }
    }
}
