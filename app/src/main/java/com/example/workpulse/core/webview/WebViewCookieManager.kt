package com.example.workpulse.core.webview

import android.webkit.CookieManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WebViewCookieManager @Inject constructor(){
    fun clearCookies(){
        val cookieManager = CookieManager.getInstance()

        cookieManager.removeAllCookies(null)
        cookieManager.flush()
    }
}