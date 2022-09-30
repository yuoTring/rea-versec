package com.versec.versecko.util

import java.lang.Exception

sealed class Response<out T> {

    object Loading : Response<Nothing>()

    data class Success <out T> (
        val data : T
        ) : Response<T>()

    data class Error (
        val errorMessage : String
        ) : Response<Nothing>()

    data class Exist <out T> (val data : T) : Response<T> ()
    data class No <out T> (val data : T) : Response<T> ()
}
