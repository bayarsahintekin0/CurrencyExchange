package com.bayarsahintekin.currencyexchange.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bayarsahintekin.bayweather.data.model.WeatherModel
@Database(entities = [WeatherModel::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productsDao() : ProductsDao

    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) { instance ?: buildDatabase(context).also { instance = it } }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "products")
                .fallbackToDestructiveMigration()
                .build()
    }

}