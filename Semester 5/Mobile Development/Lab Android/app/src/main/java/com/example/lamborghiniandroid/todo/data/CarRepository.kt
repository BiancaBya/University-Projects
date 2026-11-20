package com.example.lamborghiniandroid.todo.data

import android.util.Log
import com.example.lamborghiniandroid.core.TAG
import com.example.lamborghiniandroid.core.data.remote.Api
import com.example.lamborghiniandroid.todo.data.local.CarDao
import com.example.lamborghiniandroid.todo.data.remote.CarEvent
import com.example.lamborghiniandroid.todo.data.remote.CarService
import com.example.lamborghiniandroid.todo.data.remote.CarWsClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

class CarRepository(
    private val carService: CarService,
    private val carWsClient: CarWsClient,
    private val carDao: CarDao
) {
    private val _cars = MutableStateFlow<List<Car>>(emptyList())
    val carStream: StateFlow<List<Car>> = _cars

    private fun bearer() = "Bearer ${Api.tokenInterceptor.token}"

    init {
        Log.d(TAG, "CarRepository init")
    }

    // -------- REST --------

    suspend fun refresh() {
        Log.d(TAG, "CarRepository.refresh")
        try {
            val pending = carDao.getPending()
            if (pending.isNotEmpty()) {
                Log.d(TAG, "Found ${pending.size} pending local cars to sync")
                for (localCar in pending) {
                    try {
                        val carForServer = localCar.copy(id = null)
                        val created = carService.create(bearer(), carForServer)

                        localCar.id?.let { oldId ->
                            carDao.deleteById(oldId)
                        }
                        carDao.insert(created)
                    } catch (e: Exception) {
                        Log.w(TAG, "Failed to sync local car ${localCar.id}", e)
                    }
                }
            }

            val cars = carService.find(bearer())
            carDao.deleteAll()
            carDao.insert(cars)
            _cars.value = cars
            Log.d(TAG, "refresh succeeded")
        } catch (e: Exception) {
            Log.w(TAG, "refresh failed", e)
            throw e
        }
    }


    suspend fun getById(id: Long): Car? {
        Log.d(TAG, "CarRepository.getById $id")
        return try {
            carService.read(bearer(), id)
        } catch (e: Exception) {
            Log.w(TAG, "getById failed, falling back to local", e)
            carDao.getByIdLocal(id)
        }
    }

    suspend fun saveLocal(car: Car): Car {
        Log.d(TAG, "CarRepository.saveLocal $car")
        val localId = car.id ?: -System.currentTimeMillis()
        val localCar = car.copy(id = localId)

        carDao.insert(localCar)
        _cars.value = listOf(localCar) + _cars.value.filterNot { it.id == localId }

        return localCar
    }



    suspend fun save(car: Car): Car {
        return if (car.id == null) create(car) else update(car)
    }

    private suspend fun create(car: Car): Car {
        Log.d(TAG, "CarRepository.create $car")
        return try {
            val created = carService.create(bearer(), car)
            carDao.insert(created)
            _cars.value = listOf(created) + _cars.value
            created
        } catch (e: Exception) {
            Log.w(TAG, "create failed, saving local only", e)
            saveLocal(car)
        }
    }


    private suspend fun update(car: Car): Car {
        val id = requireNotNull(car.id)
        Log.d(TAG, "CarRepository.update $car")
        return try {
            val updated = carService.update(bearer(), id, car)
            carDao.update(updated)
            _cars.value = _cars.value.map { if (it.id == id) updated else it }
            updated
        } catch (e: Exception) {
            Log.w(TAG, "update failed, updating local only", e)
            carDao.insert(car)
            _cars.value = _cars.value.map { if (it.id == id) car else it }
            car
        }
    }


    suspend fun delete(id: Long) {
        Log.d(TAG, "CarRepository.delete $id")
        carService.delete(bearer(), id)
        carDao.deleteById(id)
        _cars.value = _cars.value.filterNot { it.id == id }
    }

    suspend fun deleteAll() {
        Log.d(TAG, "CarRepository.deleteAll")
        carDao.deleteAll()
        _cars.value = emptyList()
    }

    // -------- WebSocket --------

    fun setToken(token: String) {
        carWsClient.authorize(token)
    }

    suspend fun openWsClient() {
        Log.d(TAG, "CarRepository.openWsClient")
        withContext(Dispatchers.IO) {
            getCarEvents().collectLatest { result ->
                if (result.isSuccess) {
                    val event = result.getOrNull()
                    if (event != null) {
                        handleEvent(event)
                    }
                } else {
                    Log.w(TAG, "Car event error", result.exceptionOrNull())
                }
            }
        }
    }

    private suspend fun handleEvent(event: CarEvent) {
        when (event.type) {
            "WS_INSERT", "INSERT", "created" -> {
                val c = event.car ?: return
                carDao.insert(c)
                _cars.value = listOf(c) + _cars.value.filterNot { it.id == c.id }
            }
            "WS_UPDATE", "UPDATE", "updated" -> {
                val c = event.car ?: return
                carDao.update(c)
                _cars.value = _cars.value.map { if (it.id == c.id) c else it }
            }
            "WS_DELETE", "DELETE", "deleted" -> {
                val id = event.id ?: event.car?.id ?: return
                carDao.deleteById(id)
                _cars.value = _cars.value.filterNot { it.id == id }
            }
        }
    }


    suspend fun closeWsClient() {
        Log.d(TAG, "CarRepository.closeWsClient")
        withContext(Dispatchers.IO) {
            carWsClient.closeSocket()
        }
    }

    private suspend fun getCarEvents(): Flow<Result<CarEvent>> = callbackFlow {
        Log.d(TAG, "CarRepository.getCarEvents started")

        carWsClient.openSocket(
            onEvent = { event ->
                if (event != null) {
                    trySend(Result.success(event))
                }
            },
            onClosed = { close() },
            onFailure = { close() }
        )

        awaitClose {
            carWsClient.closeSocket()
        }
    }
}


