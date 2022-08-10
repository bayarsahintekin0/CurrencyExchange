package com.bayarsahintekin.bayweather.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weathers")
data class WeatherModel(
    var rh: Double? = null,
    var pod: String? = null,
    var lon: Double? = null,
    var pres: Double? = null,
    var timezone: String? = null,
    var obTime: String? = null,
    var countryCode: String? = null,
    var clouds: Double? = null,
    @PrimaryKey
    var ts: Double = 0.0,
    var solarRad: Double? = null,
    var stateCode: String? = null,
    var cityName: String? = null,
    var windSpd: Double? = null,
    var windCdirFull: String? = null,
    var windCdir: String? = null,
    var slp: Double? = null,
    var vis: Double? = null,
    var hAngle: Double? = null,
    var sunset: String? = null,
    var dni: Double? = null,
    var dewpt: Double? = null,
    var snow: Double? = null,
    var uv: Double? = null,
    var precip: Double? = null,
    var windDir: Double? = null,
    var sunrise: String? = null,
    var ghi: Double? = null,
    var dhi: Double? = null,
    var aqi: Double? = null,
    var lat: Double? = null,
    //var weather: Weather? = null,
    var datetime: String? = null,
    var temp: Double? = null,
    var station: String? = null,
    var elevAngle: Double? = null,
    var appTemp: Double? = null
)
