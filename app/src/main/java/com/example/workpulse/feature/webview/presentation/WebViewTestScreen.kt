package com.example.workpulse.feature.webview.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.workpulse.core.ui.components.AppWebView


@Composable
fun WebViewTestScreen (){
    AppWebView(
        url = "http://10.65.181.19:8000/app/home",
        modifier = Modifier.fillMaxSize()
    )
}