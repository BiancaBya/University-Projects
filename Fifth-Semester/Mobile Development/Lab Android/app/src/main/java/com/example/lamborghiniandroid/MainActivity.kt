package com.example.lamborghiniandroid

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.material3.Surface
import androidx.lifecycle.lifecycleScope
import com.example.lamborghiniandroid.core.TAG
import com.example.lamborghiniandroid.notifications.NotificationsScreen
import com.example.lamborghiniandroid.network.NetworkStatusScreen
import com.example.lamborghiniandroid.worker.CarJobsScreen
import com.example.lamborghiniandroid.ui.theme.LamborghiniAndroidTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.d(TAG, "onCreate")
            LamborghiniApp {
//              NotificationsScreen()
//              NetworkStatusScreen()
//              CarJobsScreen()

                MyAppNavHost()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            (application as MyApplication).container.carRepository.openWsClient()
        }
    }

    override fun onPause() {
        super.onPause()
        lifecycleScope.launch {
            (application as MyApplication).container.carRepository.closeWsClient()
        }
    }
}

@Composable
fun LamborghiniApp(content: @Composable () -> Unit) {
    LamborghiniAndroidTheme {
        Surface {
            content()
        }
    }
}
