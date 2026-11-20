package com.example.lamborghiniandroid.notifications

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun NotificationsScreen() {
    val context = LocalContext.current
    val channelId = "LamboChannel"
    val notificationId = 1

    // creăm canalul o singură dată
    LaunchedEffect(Unit) {
        createNotificationChannel(channelId, context)
    }

    // launcher pentru cererea de permisiune
    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            // aici ai putea pune un log / snackbar dacă vrei
            // momentan nu facem nimic special
        }

    fun ensureNotificationPermissionOrRequest(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPermission) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                return false
            }
        }
        return true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(
            "Lamborghini notifications demo",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // simple notification
        Button(
            onClick = {
                if (ensureNotificationPermissionOrRequest()) {
                    showSimpleNotification(
                        context,
                        channelId,
                        notificationId,
                        "Simple notification",
                        "This is a simple Lamborghini notification."
                    )
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Simple notification")
        }

        // notification with tap action
        Button(
            onClick = {
                if (ensureNotificationPermissionOrRequest()) {
                    showSimpleNotificationWithTapAction(
                        context,
                        channelId,
                        notificationId + 1,
                        "Tap notification",
                        "Tap to open the app."
                    )
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Notification + tap action")
        }
    }
}

