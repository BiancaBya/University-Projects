package com.example.lamborghiniandroid.todo.data.remote

import com.example.lamborghiniandroid.todo.data.Car

data class CarEvent(
    val type: String,
    val car: Car?,
    val id: Long? = null
)
