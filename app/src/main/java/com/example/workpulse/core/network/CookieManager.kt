package com.example.workpulse.core.network

import com.example.workpulse.core.datastore.CookieStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CookieManager @Inject constructor(
    private val cookieStorage: CookieStorage
) : CookieJar {

    override fun saveFromResponse(
        url: HttpUrl,
        cookies: List<Cookie>
    ) {

        cookies.firstOrNull {
            it.name == "sid"
        }?.let {

            runBlocking {
                cookieStorage.saveSid(it.value)
            }

        }
    }

    override fun loadForRequest(
        url: HttpUrl
    ): List<Cookie> {

        val sid = runBlocking {
            cookieStorage.getSid()
        }

        if (sid.isBlank()) {
            return emptyList()
        }

        return listOf(

            Cookie.Builder()
                .name("sid")
                .value(sid)
                .domain(url.host)
                .path("/")
                .httpOnly()
                .build()

        )
    }

    fun clearCookies() {

        runBlocking {
            cookieStorage.clear()
        }

    }

}