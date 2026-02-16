
package com.o7services.gurmatjeevanjaach.retrofit

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class LanguageInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val lang = prefs.getString("language", "pa") ?: "pa"

        val request = chain.request().newBuilder()
            .addHeader("Accept-Language", lang)
            .build()

        return chain.proceed(request)
    }
}
