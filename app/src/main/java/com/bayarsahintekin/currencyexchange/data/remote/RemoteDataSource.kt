package com.bayarsahintekin.currencyexchange.data.remote

import com.bayarsahintekin.currencyexchange.utils.BaseDataSource

class RemoteDataSource(private val service: IService): BaseDataSource() {

    suspend fun getCurrencies(base: String? = null,symbols: String? = null) = getResult { service.getCurrencies(base,symbols) }
}