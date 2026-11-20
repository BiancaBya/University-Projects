package com.example.lamborghiniandroid.worker

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.lamborghiniandroid.core.TAG
import kotlinx.coroutines.launch
import java.util.UUID

data class CarJobUiState(
    val isRunning: Boolean = false,
    val progress: Int = 0,
    val result: Int = 0
)

class CarJobsViewModel(application: Application) : AndroidViewModel(application) {

    var uiState = androidx.compose.runtime.mutableStateOf(CarJobUiState())
        private set

    private val workManager: WorkManager = WorkManager.getInstance(getApplication())
    private var workId: UUID? = null

    init {
        startJob()
    }

    private fun startJob() {
        viewModelScope.launch {
            Log.d(TAG, "CarJobsViewModel.startJob")

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val inputData = Data.Builder()
                .putInt("to", 10)
                .build()

            val myWork = OneTimeWorkRequest.Builder(CarJobWorker::class.java)
                .setConstraints(constraints)
                .setInputData(inputData)
                .build()

            workId = myWork.id
            uiState.value = uiState.value.copy(isRunning = true)

            workManager.apply {
                enqueue(myWork)

                getWorkInfoByIdLiveData(workId!!).asFlow().collect { info ->
                    Log.d(TAG, "CarJobsViewModel workInfo: $info")
                    uiState.value = uiState.value.copy(
                        isRunning = !info.state.isFinished,
                        progress = info.progress.getInt("progress", 0),
                    )
                    if (info.state.isFinished) {
                        uiState.value = uiState.value.copy(
                            result = info.outputData.getInt("result", 0),
                        )
                    }
                }
            }
        }
    }

    fun cancelJob() {
        workId?.let {
            Log.d(TAG, "CarJobsViewModel.cancelJob $it")
            workManager.cancelWorkById(it)
        }
    }

    companion object {
        fun Factory(application: Application): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                CarJobsViewModel(application)
            }
        }
    }
}

@Composable
fun CarJobsScreen() {
    val vm = viewModel<CarJobsViewModel>(
        factory = CarJobsViewModel.Factory(
            LocalContext.current.applicationContext as Application
        )
    )
    val state = vm.uiState.value

    Column {
        Text(
            text = "Job state: $state",
            style = MaterialTheme.typography.bodyMedium
        )
        Button(onClick = { vm.cancelJob() }) {
            Text("Cancel job")
        }
    }
}
