package com.example.lamborghiniandroid.location

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

private const val TAG = "MyMap"

@Composable
fun MyMap(
    lat: Double,
    lng: Double,
    modifier: Modifier = Modifier,
    onPickLocation: (Double, Double) -> Unit = { _, _ -> }
) {
    val markerState = rememberMarkerState(position = LatLng(lat, lng))
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerState.position, 10f)
    }

    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        onMapClick = { clicked ->
            Log.d(TAG, "onMapClick $clicked")
            markerState.position = clicked
            onPickLocation(clicked.latitude, clicked.longitude)
        },
        onMapLongClick = { clicked ->
            Log.d(TAG, "onMapLongClick $clicked")
            markerState.position = clicked
            onPickLocation(clicked.latitude, clicked.longitude)
        }
    ) {
        Marker(
            state = markerState,
            title = "Selected location",
            snippet = "${markerState.position.latitude}, ${markerState.position.longitude}"
        )
    }
}


