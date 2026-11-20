package com.example.lamborghiniandroid

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lamborghiniandroid.core.TAG
import com.example.lamborghiniandroid.core.data.UserPreferences
import com.example.lamborghiniandroid.core.data.UserPreferencesRepository
import com.example.lamborghiniandroid.todo.data.CarRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MyAppViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val carRepository: CarRepository
) : ViewModel() {

    private var wsJob: Job? = null

    init {
        Log.d(TAG, "MyAppViewModel init")
    }

    fun logout() {
        viewModelScope.launch {
            carRepository.deleteAll()
            carRepository.closeWsClient()
            userPreferencesRepository.save(UserPreferences())
        }
    }

    fun setToken(token: String) {
        carRepository.setToken(token)
        startWs()
    }

    private fun startWs() {
        if (wsJob?.isActive == true) return
        wsJob = viewModelScope.launch {
            try {
                carRepository.openWsClient()
            } catch (e: Exception) {
                Log.w(TAG, "startWs failed", e)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                MyAppViewModel(
                    app.container.userPreferencesRepository,
                    app.container.carRepository
                )
            }
        }
    }
}


