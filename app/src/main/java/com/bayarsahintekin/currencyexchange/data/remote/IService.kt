package com.bayarsahintekin.currencyexchange.data.remote

import com.bayarsahintekin.currencyexchange.data.model.Currency
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IService {

    /**
     *  @param base : currency you have
     *  @param symbols: strings list separated by comma , opposite currencies.
     */
    @GET("latest")
    suspend fun getCurrencies(@Query("base")  base: String?,
                              @Query("symbols") symbols: String?):Response<Currency>
}