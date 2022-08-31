package com.versec.versecko.util

import java.lang.Exception

sealed class Results<out T: Any> {
    data class Success <out T : Any> (val parameter : T) : Results<T>()
    data class Error <out T : Any> (val exception: Exception?) : Results<T>()
}
