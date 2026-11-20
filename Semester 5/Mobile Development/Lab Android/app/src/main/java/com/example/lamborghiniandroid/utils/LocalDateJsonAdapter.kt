package com.example.lamborghiniandroid.utils

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate

/**
 LocalDate <-> "YYYY-MM-DD"
 */
class LocalDateJsonAdapter {

    @ToJson
    fun toJson(value: LocalDate?): String? =
        value?.toString()

    @FromJson
    fun fromJson(value: String?): LocalDate? =
        value?.let { LocalDate.parse(it) }
}


