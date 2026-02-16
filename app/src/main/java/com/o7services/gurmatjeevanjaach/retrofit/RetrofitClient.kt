package com.o7services.gurmatjeevanjaach.retrofit

import com.google.firebase.appdistribution.gradle.ApiService
import com.o7services.gurmatjeevanjaach.consts.AppConst.baseUrl
import android.content.Context
import com.o7services.gurmatjeevanjaach.consts.AppConst.baseUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var retrofit: Retrofit? = null

    fun instance(context: Context): RetrofitInterface {

        if (retrofit == null) {

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(LanguageInterceptor(context.applicationContext))
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit!!.create(RetrofitInterface::class.java)
    }
}
