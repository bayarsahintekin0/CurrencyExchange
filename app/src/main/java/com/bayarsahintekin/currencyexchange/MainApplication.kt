package com.bayarsahintekin.currencyexchange

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MainApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}