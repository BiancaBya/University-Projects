package com.example.lamborghiniandroid

import android.app.Application
import android.util.Log
import com.example.lamborghiniandroid.AppContainer
import com.example.lamborghiniandroid.core.TAG

class MyApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "MyApplication init")
        container = AppContainer(this)
    }
}
