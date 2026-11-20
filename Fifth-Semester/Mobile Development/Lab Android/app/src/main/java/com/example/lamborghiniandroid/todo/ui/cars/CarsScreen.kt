package com.example.lamborghiniandroid.todo.ui.cars

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.asFlow
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.lamborghiniandroid.network.ConnectivityManagerNetworkMonitor
import com.example.lamborghiniandroid.notifications.createNotificationChannel
import com.example.lamborghiniandroid.notifications.showSimpleNotification
import com.example.lamborghiniandroid.todo.data.Car
import com.example.lamborghiniandroid.worker.CarJobWorker
import kotlinx.coroutines.launch
import java.util.UUID

data class CarJobUiState(
    val isRunning: Boolean = false,
    val progress: Int = 0,
    val result: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarsScreen(
    onCarClick: (Long) -> Unit,
    onAddCar: () -> Unit,
    onLogout: () -> Unit,
    onOpenLocation: () -> Unit = {},
    onOpenCamera: () -> Unit = {},
    onOpenSensors: () -> Unit = {},
    onOpenAnimations: () -> Unit = {}
){
    val vm: CarsViewModel = viewModel(factory = CarsViewModel.Factory)
    val state by vm.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // ------------------ Network status ------------------

    var isOnline by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        ConnectivityManagerNetworkMonitor(context.applicationContext)
            .isOnline
            .collect { online ->
                isOnline = online
            }
    }

    // ------------------ Notifications ------------------

    val channelId = remember { "LamboChannel" }
    val notificationId = 1

    LaunchedEffect(Unit) {
        createNotificationChannel(channelId, context)
    }

    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { }

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

    // ------------------ WorkManager ------------------

    val workManager = remember { WorkManager.getInstance(context.applicationContext) }
    var jobState by remember { mutableStateOf(CarJobUiState()) }
    var currentWorkId by remember { mutableStateOf<UUID?>(null) }

    fun startJob() {
        scope.launch {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val inputData = Data.Builder()
                .putInt("to", 10)
                .build()

            val request = OneTimeWorkRequestBuilder<CarJobWorker>()
                .setConstraints(constraints)
                .setInputData(inputData)
                .build()

            currentWorkId = request.id
            jobState = CarJobUiState(isRunning = true, progress = 0, result = 0)

            workManager.enqueue(request)

            workManager.getWorkInfoByIdLiveData(request.id).asFlow().collect { info ->
                jobState = jobState.copy(
                    isRunning = !info.state.isFinished
                )
            }
        }
    }

    fun cancelJob() {
        currentWorkId?.let {
            workManager.cancelWorkById(it)
            jobState = jobState.copy(isRunning = false)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cars") },
                actions = {
                    TextButton(
                        onClick = {
                            if (ensureNotificationPermissionOrRequest()) {
                                showSimpleNotification(
                                    context = context,
                                    channelId = channelId,
                                    notificationId = notificationId,
                                    textTitle = "Lamborghini",
                                    textContent = "You are cool!!."
                                )
                            }
                        }
                    ) {
                        Text("Notify")
                    }

                    TextButton(onClick = onLogout) {
                        Text("Logout")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddCar) {
                Text("+")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                state.loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                state.error != null -> Text(
                    text = state.error ?: "Error",
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Is online: $isOnline",
                        )

                        HorizontalDivider()

                        Text("Background sync running: ${jobState.isRunning}")

                        androidx.compose.foundation.layout.Row {
                            Button(
                                onClick = { startJob() },
                                enabled = !jobState.isRunning
                            ) {
                                Text("Start job")
                            }
                            androidx.compose.foundation.layout.Spacer(
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Button(
                                onClick = { cancelJob() },
                                enabled = jobState.isRunning
                            ) {
                                Text("Cancel job")
                            }
                        }

                        HorizontalDivider()

                        Text("Yey:")

                        androidx.compose.foundation.layout.Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = onOpenLocation) { Text("Location & Map") }
                            androidx.compose.foundation.layout.Spacer(Modifier.padding(horizontal = 4.dp))
                            Button(onClick = onOpenCamera) { Text("Camera") }
                        }

                        androidx.compose.foundation.layout.Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(onClick = onOpenSensors) { Text("Sensors") }
                            androidx.compose.foundation.layout.Spacer(Modifier.padding(horizontal = 4.dp))
                            Button(onClick = onOpenAnimations) { Text("Animations") }
                        }

                        HorizontalDivider()

                        CarList(
                            cars = state.cars,
                            onCarClick = onCarClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CarList(
    cars: List<Car>,
    onCarClick: (Long) -> Unit
) {
    LazyColumn {
        items(cars) { car ->
            androidx.compose.material3.ListItem(
                headlineContent = { Text(car.model) },
                supportingContent = {
                    Text("${car.manufacturingDate} • owned=${car.owned}")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { car.id?.let(onCarClick) }
            )
            HorizontalDivider()
        }
    }
}


