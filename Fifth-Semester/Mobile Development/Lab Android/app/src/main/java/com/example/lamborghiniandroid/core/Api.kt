package com.example.lamborghiniandroid.core

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {
    private const val url = "10.0.2.2:8080"
    private val httpUrl = "http://$url/"
    val wsUrl = "ws://$url/"

    private val gson = GsonBuilder().create()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(httpUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .build()
}
