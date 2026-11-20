package com.example.lamborghiniandroid

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import com.example.lamborghiniandroid.auth.data.AuthRepository
import com.example.lamborghiniandroid.auth.data.remote.AuthDataSource
import com.example.lamborghiniandroid.core.TAG
import com.example.lamborghiniandroid.core.data.UserPreferencesRepository
import com.example.lamborghiniandroid.core.data.remote.Api
import com.example.lamborghiniandroid.todo.data.CarRepository
import com.example.lamborghiniandroid.todo.data.remote.CarService
import com.example.lamborghiniandroid.todo.data.remote.CarWsClient

val Context.userPreferencesDataStore by preferencesDataStore(
    name = "user_preferences"
)

class AppContainer(val context: Context) {

    init {
        Log.d(TAG, "AppContainer init")
    }

    private val carService: CarService = Api.retrofit.create(CarService::class.java)
    private val carWsClient: CarWsClient = CarWsClient(Api.okHttpClient)
    private val authDataSource: AuthDataSource = AuthDataSource()

    private val database: MyAppDatabase by lazy {
        MyAppDatabase.getDatabase(context)
    }

    val carRepository: CarRepository by lazy {
        CarRepository(carService, carWsClient, database.carDao())
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository(authDataSource)
    }

    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context.userPreferencesDataStore)
    }
}


