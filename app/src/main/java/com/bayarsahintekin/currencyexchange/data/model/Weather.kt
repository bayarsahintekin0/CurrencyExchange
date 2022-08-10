package com.bayarsahintekin.bayweather.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

@Entity(tableName = "weather")
data class Weather (
    var icon: String? = null,
    @PrimaryKey
    var code: Int? = null,
    var description: String? = null
)

