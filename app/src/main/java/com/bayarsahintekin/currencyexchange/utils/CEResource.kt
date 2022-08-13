package com.bayarsahintekin.currencyexchange.utils

data class CEResource<out T>(val status: Status,val data: T?, val message: String?){

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T? = null): CEResource<T>{
            return CEResource(Status.SUCCESS,data,null)
        }

        fun <T> error(message: String, data: T? = null): CEResource<T>{
            return CEResource(Status.ERROR,data,message)
        }

        fun <T> loading(data: T? = null):CEResource<T>{
            return CEResource(Status.LOADING,data,null)
        }
    }
}