package com.example.lamborghiniandroid.core.data.remote

import com.example.lamborghiniandroid.utils.LocalDateAdapter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate

object Api {
    private const val url = "10.0.2.2:8080"

    private val httpUrl = "http://$url/"
    val wsUrl = "ws://$url/cars-ws/websocket"

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .create()

    val tokenInterceptor = TokenInterceptor()

    val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(tokenInterceptor)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(httpUrl)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}
