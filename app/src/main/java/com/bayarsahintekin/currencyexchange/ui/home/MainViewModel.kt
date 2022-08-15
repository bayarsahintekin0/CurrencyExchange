package com.bayarsahintekin.currencyexchange.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bayarsahintekin.currencyexchange.data.model.Currency
import com.bayarsahintekin.currencyexchange.data.repository.ICurrenciesRepository
import com.bayarsahintekin.currencyexchange.utils.CEResource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
@HiltViewModel
class MainViewModel @Inject constructor(private val currenciesRepository: ICurrenciesRepository)  : ViewModel() {

    private lateinit var _currencies: LiveData<CEResource<Currency>>
    val currencies: LiveData<CEResource<Currency>>
        get() = _currencies


    fun getCurrencies(base:String?= null,symbols:ArrayList<String>? = null){
        _currencies = currenciesRepository.getCurrencies()
    }
}