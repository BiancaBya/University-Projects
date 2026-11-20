package com.example.lamborghiniandroid.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "cars")
data class Car(
    @PrimaryKey val id: Long? = null,
    val model: String = "",
    val manufacturingDate: LocalDate? = null,
    val owned: Boolean = false,
    val photoUrl: String? = null
)
