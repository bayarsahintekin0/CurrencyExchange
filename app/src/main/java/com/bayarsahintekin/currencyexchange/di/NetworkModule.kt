package com.bayarsahintekin.currencyexchange.di

import com.bayarsahintekin.currencyexchange.data.remote.IService
import com.bayarsahintekin.currencyexchange.data.remote.RemoteDataSource
import com.bayarsahintekin.currencyexchange.data.repository.CurrenciesRepositoryImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson):Retrofit{
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val request: Request =
                chain.request().newBuilder().addHeader("apikey", "DxZ1bxY56URq1gQz9wVCVYgfxBuWyxd9")
                    .build()
            chain.proceed(request)
        }
        return Retrofit.Builder()
            .baseUrl("https://api.apilayer.com/exchangerates_data/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient.build())
            .build()
    }

    @Provides
    fun provideGson():Gson = GsonBuilder().create()

    @Provides
    fun provideService(retrofit: Retrofit): IService = retrofit.create(IService::class.java)

    @Singleton
    @Provides
    fun provideRemoteDataSource(service: IService) = RemoteDataSource(service)

    @Singleton
    @Provides
    fun provideCurrenciesRepository(remoteDataSource: RemoteDataSource) = CurrenciesRepositoryImpl(remoteDataSource)
}