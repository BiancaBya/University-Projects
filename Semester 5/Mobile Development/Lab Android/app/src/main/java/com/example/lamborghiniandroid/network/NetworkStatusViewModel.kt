package com.example.lamborghiniandroid.network

import android.app.Application
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NetworkStatusViewModel(application: Application) : AndroidViewModel(application) {

    var isOnline = androidx.compose.runtime.mutableStateOf(false)
        private set

    init {
        collectNetworkStatus()
    }

    private fun collectNetworkStatus() {
        viewModelScope.launch {
            ConnectivityManagerNetworkMonitor(getApplication())
                .isOnline
                .collectLatest { online ->
                    isOnline.value = online
                }
        }
    }

    companion object {
        fun Factory(application: Application): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                NetworkStatusViewModel(application)
            }
        }
    }
}

@Composable
fun NetworkStatusScreen() {
    val vm = viewModel<NetworkStatusViewModel>(
        factory = NetworkStatusViewModel.Factory(
            LocalContext.current.applicationContext as Application
        )
    )

    Text(
        text = "Is online: ${vm.isOnline.value}",
        style = MaterialTheme.typography.bodyMedium,
    )
}


