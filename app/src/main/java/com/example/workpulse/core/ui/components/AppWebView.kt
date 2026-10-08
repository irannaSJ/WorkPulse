package com.example.workpulse.core.ui.components

import android.graphics.Bitmap
import android.net.Uri
import android.webkit.CookieManager
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun AppWebView(
    url: String,
    sid: String,
    modifier: Modifier = Modifier,
    onExit: () -> Unit
) {
    var webView by remember { mutableStateOf<WebView?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->

                WebView(context).apply {

                    val cookieManager = CookieManager.getInstance()

                    cookieManager.setAcceptCookie(true)
                    cookieManager.setAcceptThirdPartyCookies(this, true)

                    // Important: no extra space after sid=
                    cookieManager.setCookie(
                        url,
                        "sid=$sid; Path=/; HttpOnly"
                    )

                    cookieManager.flush()

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        databaseEnabled = true
                    }

                    webViewClient = object : WebViewClient() {

                        override fun onPageStarted(
                            view: WebView?,
                            url: String?,
                            favicon: Bitmap?
                        ) {
                            super.onPageStarted(view, url, favicon)

                            isLoading = true
                            hasError = false
                        }

                        override fun onPageFinished(
                            view: WebView?,
                            url: String?
                        ) {
                            super.onPageFinished(view, url)

                            isLoading = false
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            error: WebResourceError?
                        ) {
                            super.onReceivedError(view, request, error)

                            if (request?.isForMainFrame == true) {
                                isLoading = false
                                hasError = true
                            }
                        }
                    }

                    loadUrl(url)

                    webView = this
                }
            }
        )

        if (isLoading && !hasError) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (hasError) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Unable to load page",
                    style = MaterialTheme.typography.titleMedium
                )

                Button(
                    onClick = {
                        hasError = false
                        isLoading = true
                        webView?.reload()
                    }
                ) {
                    Text("Retry")
                }
            }
        }
    }

    /*
     * Android system Back handling
     *
     * React navigation:
     *
     * Home
     *   ↓
     * #section/xxxxx
     *
     * When Back is pressed:
     *
     * #section/xxxxx
     *        ↓
     *      Home
     *
     * We do NOT use WebView history for this.
     */
    BackHandler {

        val currentWebView = webView

        if (currentWebView == null) {
            onExit()
            return@BackHandler
        }

        val currentUrl = currentWebView.url

        if (currentUrl == null) {
            onExit()
            return@BackHandler
        }

        val uri = runCatching {
            Uri.parse(currentUrl)
        }.getOrNull()

        val fragment = uri?.fragment

        /*
         * Example:
         *
         * http://test.site/dynamic-ui#section/77f9fntmak
         *
         * fragment =
         * section/77f9fntmak
         */
        if (!fragment.isNullOrBlank() && fragment.startsWith("section/")) {

            /*
             * Directly return React to Home.
             *
             * This triggers the React hashchange listener:
             *
             * selectedSection -> null
             *
             * HomePage is rendered again.
             */
            currentWebView.evaluateJavascript(
                "window.location.hash = '';",
                null
            )

            return@BackHandler
        }

        /*
         * No React section is open.
         *
         * Now Android navigation should leave the WebView screen.
         */
        onExit()
    }
}