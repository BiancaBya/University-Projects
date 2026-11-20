package com.example.lamborghiniandroid.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocationMonitor(private val context: Context) {
    @SuppressLint("MissingPermission")
    val currentLocation: Flow<Location> = callbackFlow {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.lastLocation.addOnSuccessListener {
            Log.d("LocationMonitor", "lastLocation $it")
            if (it != null) trySend(it)
        }

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    Log.d("LocationMonitor", "onLocationResult $location")
                    trySend(location)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            LocationRequest.Builder(10_000).build(),
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            Log.d("LocationMonitor", "removeLocationUpdates")
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
}



