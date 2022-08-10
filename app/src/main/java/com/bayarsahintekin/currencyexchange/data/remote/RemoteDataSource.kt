package com.bayarsahintekin.bayweather.data.remote

import com.bayarsahintekin.currencyexchange.data.remote.BaseDataSource
import com.bayarsahintekin.currencyexchange.data.remote.IService
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val productService: IService
) : BaseDataSource() {
    suspend fun getWeathersByLocation(lon: Long, lat: Long) =
        getResult { productService.getWeathersByLocation(lng = lon ,lat = lat) }
}