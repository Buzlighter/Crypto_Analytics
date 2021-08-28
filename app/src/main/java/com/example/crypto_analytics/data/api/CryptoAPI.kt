package com.example.crypto_analytics.data.api

import retrofit2.http.GET

interface CryptoAPI {

    @GET("currencies/sparkline")
    suspend fun getCryptoCurrency()
}