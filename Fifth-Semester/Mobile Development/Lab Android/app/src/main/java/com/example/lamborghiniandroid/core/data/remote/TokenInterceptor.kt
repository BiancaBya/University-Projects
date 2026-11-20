package com.example.lamborghiniandroid.core.data.remote

import android.util.Log
import com.example.lamborghiniandroid.core.TAG
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {
    var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val t = token
        if (t == null) {
            Log.d(TAG, "Token is null, sending request without Authorization")
            return chain.proceed(original)
        }
        val request = original.newBuilder()
            .addHeader("Authorization", "Bearer $t")
            .build()
        Log.d(TAG, "Authorization bearer added")
        return chain.proceed(request)
    }
}
