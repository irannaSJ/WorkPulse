package com.example.workpulse.core.ui.components

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
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

private const val TAG = "AppWebView"


@RequiresApi(Build.VERSION_CODES.KITKAT)
@Composable
fun AppWebView(
    url: String,
    sid: String,
    modifier: Modifier = Modifier,
    onExit: () -> Unit
) {

    var webView by remember {
        mutableStateOf<WebView?>(null)
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    var hasError by remember {
        mutableStateOf(false)
    }


    Box(
        modifier = modifier.fillMaxSize()
    ) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),

            factory = { context ->

                WebView(context).apply {

                    /* =====================================================
                       Cookie
                       ===================================================== */

                    val cookieManager =
                        CookieManager.getInstance()

                    cookieManager.setAcceptCookie(true)

                    cookieManager.setAcceptThirdPartyCookies(
                        this,
                        true
                    )

                    cookieManager.setCookie(
                        url,
                        "sid=$sid; Path=/; HttpOnly"
                    )

                    cookieManager.flush()


                    /* =====================================================
                       WebView Settings
                       ===================================================== */

                    settings.apply {

                        javaScriptEnabled = true

                        domStorageEnabled = true

                        databaseEnabled = true
                    }


                    /* =====================================================
                       WebView Client
                       ===================================================== */

                    webViewClient =
                        object : WebViewClient() {

                            override fun onPageStarted(
                                view: WebView?,
                                url: String?,
                                favicon: Bitmap?
                            ) {
                                super.onPageStarted(
                                    view,
                                    url,
                                    favicon
                                )

                                isLoading = true
                                hasError = false

                                Log.d(
                                    TAG,
                                    "Page started: $url"
                                )
                            }


                            override fun onPageFinished(
                                view: WebView?,
                                url: String?
                            ) {
                                super.onPageFinished(
                                    view,
                                    url
                                )

                                isLoading = false

                                Log.d(
                                    TAG,
                                    "Page finished: $url"
                                )
                            }


                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(
                                    view,
                                    request,
                                    error
                                )

                                if (
                                    request?.isForMainFrame == true
                                ) {

                                    isLoading = false
                                    hasError = true

                                    Log.e(
                                        TAG,
                                        "Main page error: ${error?.description}"
                                    )
                                }
                            }
                        }


                    /* =====================================================
                       Initial URL
                       ===================================================== */

                    Log.d(
                        TAG,
                        "Loading URL: $url"
                    )

                    loadUrl(url)

                    webView = this
                }
            }
        )


        /* =============================================================
           Loading
           ============================================================= */

        if (
            isLoading &&
            !hasError
        ) {

            CircularProgressIndicator(
                modifier = Modifier.align(
                    Alignment.Center
                )
            )
        }


        /* =============================================================
           Error
           ============================================================= */

        if (hasError) {

            Column(
                modifier =
                    Modifier.align(
                        Alignment.Center
                    ),

                horizontalAlignment =
                    Alignment.CenterHorizontally,

                verticalArrangement =
                    Arrangement.Center
            ) {

                Text(
                    text = "Unable to load page",
                    style =
                        MaterialTheme.typography.titleMedium
                )

                Button(
                    onClick = {

                        hasError = false
                        isLoading = true

                        webView?.reload()
                    }
                ) {

                    Text(
                        text = "Retry"
                    )
                }
            }
        }
    }


    /* ================================================================
       Android System Back
       ================================================================ */

    BackHandler {

        val currentWebView = webView

        if (currentWebView == null) {

            Log.d(
                TAG,
                "Back -> WebView is null -> Exit"
            )

            onExit()

            return@BackHandler
        }


        /*
         * Ask the React application which route
         * is currently active.
         *
         * Home:
         *
         *     #/
         *
         * Form:
         *
         *     #/form/xxxxx
         *
         * List:
         *
         *     #/list/xxxxx
         */

//        currentWebView.evaluateJavascript(
//            """
//            (function() {
//
//                const hash = window.location.hash;
//
//                if (
//                    hash &&
//                    hash !== "#" &&
//                    hash !== "#/"
//                ) {
//                    window.history.back();
//                    return "ROUTER_BACK";
//                }
//
//                return "EXIT";
//
//            })();
//            """.trimIndent()
//        ) { result ->
//
//            Log.d(
//                TAG,
//                "Back JS result: $result"
//            )
//
//            if (
//                result
//                    ?.trim()
//                    ?.removeSurrounding("\"") == "EXIT"
//            ) {
//
//                Log.d(
//                    TAG,
//                    "Back -> React Home -> Exit Android"
//                )
//
//                onExit()
//            }
//        }


        currentWebView.evaluateJavascript(
            """
    (function() {
        const hash = window.location.hash;
        const isHome = !hash || hash === "#" || hash === "#/";
        if (!isHome){
            window.history.back();
            return "ROUTER_BACK";
        }
        return "EXIT";
    })();
    """.trimIndent()
        ) { result ->

            val actions = result
                ?.trim()
                ?.removeSurrounding("\"")

            Log.d(
                TAG,
                "BACK: $actions"
            )

            if(actions == "EXIT"){
                Log.d(TAG, "Back -> Home -> Exit Android")
                onExit()
            }
        }
    }
}