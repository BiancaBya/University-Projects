package com.example.lamborghiniandroid.location

import android.app.Application
import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.lamborghiniandroid.utils.LocationMonitor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class MapUiState(
    val lat: Double? = null,
    val lng: Double? = null,
    val source: String = "loading"
)

class MyLocationViewModel(application: Application) : AndroidViewModel(application) {

    var uiState by mutableStateOf(MapUiState())
        private set

    private val store = MapLocationStore(getApplication())

    init {
        loadInitialLocation()
    }

    private fun loadInitialLocation() {
        viewModelScope.launch {
            val saved = store.savedLocation.first()
            if (saved != null) {
                uiState = MapUiState(saved.lat, saved.lng, source = "saved")
                return@launch
            }

            LocationMonitor(getApplication()).currentLocation.collect { loc: Location ->
                Log.d("MyLocationViewModel", "gps $loc")
                if (uiState.lat == null || uiState.lng == null) {
                    uiState = MapUiState(loc.latitude, loc.longitude, source = "gps")
                }
            }
        }
    }

    fun setLocationFromMapClick(lat: Double, lng: Double) {
        uiState = uiState.copy(lat = lat, lng = lng, source = "saved")
        viewModelScope.launch {
            store.save(lat, lng)
        }
    }

    fun resetToGps() {
        viewModelScope.launch {
            store.clear()
            uiState = MapUiState()
        }
    }

    companion object {
        fun Factory(application: Application): ViewModelProvider.Factory = viewModelFactory {
            initializer { MyLocationViewModel(application) }
        }
    }
}


