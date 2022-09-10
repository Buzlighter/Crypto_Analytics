package com.example.crypto_analytics.data.util

sealed class DataState<T>(val data: T? = null) {
    class Success<T>(data: T?): DataState<T>(data)
    class Error<T>(data: T?): DataState<T>(data)
    class Loading<T>(): DataState<T>()
}
