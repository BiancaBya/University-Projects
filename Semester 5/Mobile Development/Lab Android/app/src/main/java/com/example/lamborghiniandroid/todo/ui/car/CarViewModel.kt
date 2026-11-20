package com.example.lamborghiniandroid.todo.ui.car

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
import kotlinx.coroutines.launch
import java.time.LocalDate

data class CarUiState(
    val car: Car? = null,
    val model: String = "",
    val manufacturingDate: String = "",
    val owned: Boolean = false,
    val loading: Boolean = false,
    val saving: Boolean = false,
    val error: String? = null
)

class CarViewModel(
    private val carRepository: CarRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarUiState())
    val uiState: StateFlow<CarUiState> = _uiState.asStateFlow()

    fun load(id: Long?) {
        if (id == null) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            val car = carRepository.getById(id)
            _uiState.value = if (car != null) {
                CarUiState(
                    car = car,
                    model = car.model,
                    manufacturingDate = car.manufacturingDate?.toString() ?: "",
                    owned = car.owned,
                    loading = false
                )
            } else {
                _uiState.value.copy(loading = false, error = "Car not found")
            }
        }
    }

    fun setModel(value: String) {
        _uiState.value = _uiState.value.copy(model = value)
    }

    fun setDate(value: String) {
        _uiState.value = _uiState.value.copy(manufacturingDate = value)
    }

    fun setOwned(value: Boolean) {
        _uiState.value = _uiState.value.copy(owned = value)
    }

    fun save(onDone: () -> Unit) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(saving = true)
                val current = _uiState.value

                val date: LocalDate? = current.manufacturingDate
                    .takeIf { it.isNotBlank() }
                    ?.let { LocalDate.parse(it) }

                val car = Car(
                    id = current.car?.id,
                    model = current.model,
                    manufacturingDate = date,
                    owned = current.owned,
                    photoUrl = current.car?.photoUrl
                )
                val saved = carRepository.save(car)
                Log.d(TAG, "saved $saved")
                onDone()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(saving = false, error = e.message)
            }
        }
    }

    fun delete(onDone: () -> Unit) {
        val id = _uiState.value.car?.id ?: return
        viewModelScope.launch {
            try {
                carRepository.delete(id)
                onDone()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                CarViewModel(app.container.carRepository)
            }
        }
    }
}



