package com.bayarsahintekin.currencyexchange.data.remote

import android.location.Location
import com.bayarsahintekin.bayweather.data.model.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IService {
    @GET("current")
    suspend fun getWeathersByLocation(
        @Query("lon") lng: Long,
        @Query("lat") lat: Long
    ): Response<BaseResponse>
}