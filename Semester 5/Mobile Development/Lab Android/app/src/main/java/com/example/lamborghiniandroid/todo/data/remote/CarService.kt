package com.example.lamborghiniandroid.todo.data.remote

import com.example.lamborghiniandroid.todo.data.Car
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CarService {

    @GET("/api/cars")
    suspend fun find(
        @Header("Authorization") authorization: String
    ): List<Car>

    @GET("/api/cars/{id}")
    suspend fun read(
        @Header("Authorization") authorization: String,
        @Path("id") id: Long
    ): Car

    @Headers("Content-Type: application/json")
    @POST("/api/cars")
    suspend fun create(
        @Header("Authorization") authorization: String,
        @Body car: Car
    ): Car

    @Headers("Content-Type: application/json")
    @PUT("/api/cars/{id}")
    suspend fun update(
        @Header("Authorization") authorization: String,
        @Path("id") id: Long,
        @Body car: Car
    ): Car

    @DELETE("/api/cars/{id}")
    suspend fun delete(
        @Header("Authorization") authorization: String,
        @Path("id") id: Long
    )
}
