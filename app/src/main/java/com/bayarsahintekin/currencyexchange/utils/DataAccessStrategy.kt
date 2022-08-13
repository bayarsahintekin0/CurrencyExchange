package com.bayarsahintekin.currencyexchange.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bayarsahintekin.currencyexchange.BuildConfig
import kotlinx.coroutines.Dispatchers

private const val TAG = "CMNetwork"

fun <T> performGetOperation(networkCall: suspend () -> CEResource<T>): LiveData<CEResource<T>> = liveData(Dispatchers.IO) {
    emit(CEResource.loading())

    val responseStatus = networkCall.invoke()
    log("Network request begins")
    try {
        if (responseStatus.status == CEResource.Status.SUCCESS ) {
            if(responseStatus.data != null){
                emit(CEResource.success(responseStatus.data))
            }else{
                emit(CEResource.success())
            }
            log("Network request success")
        }else if (responseStatus.status == CEResource.Status.ERROR) {
            emit(CEResource.error(responseStatus.message!!))
            logError("Network request error")
        }
    }catch (e: Exception) {
        logError("Network request error")
        if (e.message != null)
            emit(CEResource.error(e.message!!))
    }


}

private fun log(logMessage: String) {
    onDebug {
        Log.i(TAG, logMessage)
    }
}

private fun logError(logMessage: String) {
    onDebug {
        Log.e(TAG, logMessage)
    }
}

private fun onDebug(block: () -> Unit) {
    if (BuildConfig.DEBUG) {
        block.invoke()
    }
}