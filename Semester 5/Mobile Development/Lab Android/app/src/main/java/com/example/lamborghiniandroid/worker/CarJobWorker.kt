package com.example.lamborghiniandroid.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.lamborghiniandroid.MyApplication
import kotlinx.coroutines.runBlocking

class CarJobWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d("CarJobWorker", "doWork started - calling CarRepository.refresh()")

        val app = applicationContext as MyApplication
        val carRepository = app.container.carRepository

        return try {
            runBlocking {
                carRepository.refresh()
            }

            Log.d("CarJobWorker", "doWork finished - refresh OK")
            Result.success()
        } catch (e: Exception) {
            Log.e("CarJobWorker", "doWork failed", e)
            Result.retry()
        }
    }
}

