package com.bayarsahintekin.bayweather.data.repository

import com.bayarsahintekin.currencyexchange.data.local.ProductsDao
import com.bayarsahintekin.bayweather.data.remote.RemoteDataSource
import com.bayarsahintekin.bayweather.utils.performGetOperation
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: ProductsDao
) {
    fun getWeatherByLocation(lon: Long, lat: Long) = performGetOperation(
        databaseQuery = { localDataSource.getAllCharacters() },
        networkCall = { remoteDataSource.getWeathersByLocation(lon = lon ,lat = lat) },
        saveCallResult = { it.data?.let { it1 -> localDataSource.insertAll(it1) } }
    )


}