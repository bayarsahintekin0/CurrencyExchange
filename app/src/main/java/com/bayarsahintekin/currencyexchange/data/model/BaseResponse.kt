package com.bayarsahintekin.bayweather.data.model

data class BaseResponse (
    var data: List<WeatherModel>? = null,
    var count: Int? = null
)