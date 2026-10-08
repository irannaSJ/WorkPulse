package com.example.workpulse.feature.webview.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.workpulse.core.ui.components.AppWebView

@Composable
fun WebViewTestScreen(
    onExit: () -> Unit,
    viewModel: WebViewAuthViewModel = hiltViewModel()
) {

    val sid by viewModel.sid.collectAsStateWithLifecycle()

    when {
        sid == null -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }

        sid!!.isBlank() -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("WebView authentication session not available")
            }
        }

        else -> {
            AppWebView(
                url = "http://10.229.153.19:8000/dynamic-ui",
                sid = sid!!,
                onExit = onExit,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}