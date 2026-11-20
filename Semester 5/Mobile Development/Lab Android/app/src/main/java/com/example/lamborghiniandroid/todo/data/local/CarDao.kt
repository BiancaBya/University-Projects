package com.example.lamborghiniandroid.todo.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.lamborghiniandroid.todo.data.Car
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {
    @Query("SELECT * FROM Cars")
    fun getAll(): Flow<List<Car>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(car: Car)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cars: List<Car>)

    @Update
    suspend fun update(car: Car): Int

    @Query("DELETE FROM Cars WHERE id = :id")
    suspend fun deleteById(id: Long): Int

    @Query("DELETE FROM Cars")
    suspend fun deleteAll()

    @Query("SELECT * FROM cars WHERE id = :id")
    suspend fun getByIdLocal(id: Long): Car?

    @Query("SELECT * FROM cars WHERE id < 0")
    suspend fun getPending(): List<Car>

}


