package com.bayarsahintekin.currencyexchange.data.remote
import android.util.Log
import com.bayarsahintekin.bayweather.utils.Resource
import retrofit2.Response

abstract class BaseDataSource {
    val TAG = "BaseDataSource"
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.success(body)
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        Log.e(TAG,message)
        return Resource.error("Network call has failed for a following reason: $message")
    }

}