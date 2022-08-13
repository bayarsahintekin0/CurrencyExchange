package com.bayarsahintekin.currencyexchange.data.repository

import androidx.lifecycle.LiveData
import com.bayarsahintekin.currencyexchange.data.model.Currency
import com.bayarsahintekin.currencyexchange.data.remote.RemoteDataSource
import com.bayarsahintekin.currencyexchange.utils.CEResource
import com.bayarsahintekin.currencyexchange.utils.performGetOperation
import javax.inject.Inject

class CurrenciesRepositoryImpl @Inject constructor(private val remoteDataSource: RemoteDataSource):ICurrenciesRepository {

    override fun getCurrencies(): LiveData<CEResource<Currency>> {
        return performGetOperation (networkCall = { remoteDataSource.getCurrencies() })
    }
}