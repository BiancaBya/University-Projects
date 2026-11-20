package com.example.lamborghiniandroid.location

import android.Manifest
import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lamborghiniandroid.utils.RequirePermissions
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MyLocationScreen(modifier: Modifier = Modifier) {
    RequirePermissions(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ),
        modifier = modifier
    ) {
        val vm = viewModel<MyLocationViewModel>(
            factory = MyLocationViewModel.Factory(
                LocalContext.current.applicationContext as Application
            )
        )

        val state = vm.uiState

        if (state.lat != null && state.lng != null) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text("Source: ${state.source}")
                Button(onClick = { vm.resetToGps() }) {
                    Text("Reset to GPS")
                }
                MyMap(
                    lat = state.lat,
                    lng = state.lng,
                    modifier = Modifier.fillMaxSize(),
                    onPickLocation = { lat, lng ->
                        vm.setLocationFromMapClick(lat, lng)
                    }
                )
            }
        } else {
            LinearProgressIndicator()
        }
    }
}


