package com.example.lamborghiniandroid.todo.ui.cars

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lamborghiniandroid.MyApplication
import com.example.lamborghiniandroid.core.TAG
import com.example.lamborghiniandroid.todo.data.Car
import com.example.lamborghiniandroid.todo.data.CarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class CarsUiState(
    val cars: List<Car> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)

class CarsViewModel(
    private val carRepository: CarRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarsUiState(loading = true))
    val uiState: StateFlow<CarsUiState> = _uiState.asStateFlow()

    init {
        Log.d(TAG, "CarsViewModel init")
        viewModelScope.launch {
            try {
                carRepository.refresh()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
            carRepository.carStream.collectLatest { list ->
                _uiState.value = CarsUiState(
                    cars = list,
                    loading = false,
                    error = null
                )
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            try {
                carRepository.refresh()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(loading = false, error = e.message)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                CarsViewModel(app.container.carRepository)
            }
        }
    }
}
