package com.example.workpulse.core.ui.components


import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView


@Composable
fun AppWebView (
    url: String,
    modifier : Modifier = Modifier
){
    var webView by remember { mutableStateOf<WebView?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    Box(
        modifier = modifier.fillMaxSize()
    ){
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                context ->
                WebView(context).apply {
                    webViewClient = object  : WebViewClient(){
                        override fun onPageStarted(
                            view: WebView?,
                            url: String?,
                            favicon : Bitmap?
                        ){
                            isLoading = true
                        }

                        override fun onPageFinished(
                            view: WebView?,
                            url : String?
                        ){
                            isLoading = false
                        }
                    }

                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true

                    loadUrl(url)
                    webView = this
                }
            }

        )
        if(isLoading){
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    BackHandler(
        enabled = webView?.canGoBack() == true
    ) {
        webView?.goBack()
    }
}