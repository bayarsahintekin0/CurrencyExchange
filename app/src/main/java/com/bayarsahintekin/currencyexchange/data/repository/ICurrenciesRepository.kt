package com.bayarsahintekin.currencyexchange.data.repository

import androidx.lifecycle.LiveData
import com.bayarsahintekin.currencyexchange.data.model.Currency
import com.bayarsahintekin.currencyexchange.utils.CEResource

interface ICurrenciesRepository {

    fun getCurrencies():LiveData<CEResource<Currency>>
}