package com.example.lamborghiniandroid.location

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.mapDataStore by preferencesDataStore(name = "map_prefs")

data class SavedMapLocation(
    val lat: Double,
    val lng: Double
)

class MapLocationStore(private val context: Context) {

    private object Keys {
        val LAT = doublePreferencesKey("saved_lat")
        val LNG = doublePreferencesKey("saved_lng")
    }

    val savedLocation: Flow<SavedMapLocation?> = context.mapDataStore.data.map { prefs ->
        val lat = prefs[Keys.LAT]
        val lng = prefs[Keys.LNG]
        if (lat != null && lng != null) SavedMapLocation(lat, lng) else null
    }

    suspend fun save(lat: Double, lng: Double) {
        context.mapDataStore.edit { prefs ->
            prefs[Keys.LAT] = lat
            prefs[Keys.LNG] = lng
        }
    }

    suspend fun clear() {
        context.mapDataStore.edit { prefs ->
            prefs.remove(Keys.LAT)
            prefs.remove(Keys.LNG)
        }
    }
}


