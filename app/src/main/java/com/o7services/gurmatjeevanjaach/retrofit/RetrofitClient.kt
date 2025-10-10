package com.o7services.gurmatjeevanjaach.retrofit

import com.google.firebase.appdistribution.gradle.ApiService
import com.o7services.gurmatjeevanjaach.consts.AppConst.baseUrl
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val instance: RetrofitInterface by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitInterface::class.java)
    }
}