package com.bayarsahintekin.currencyexchange.di

import com.bayarsahintekin.currencyexchange.data.repository.CurrenciesRepositoryImpl
import com.bayarsahintekin.currencyexchange.data.repository.ICurrenciesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {
    @Binds
    abstract fun provideCurrenciesRepository(currenciesRepositoryImpl: CurrenciesRepositoryImpl): ICurrenciesRepository
}