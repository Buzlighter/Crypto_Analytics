package com.example.crypto_analytics.data.api

import com.example.crypto_analytics.data.model.CryptoResponse
import com.example.crypto_analytics.data.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CryptoService {

    @GET("coins/{id}/market_chart")
    suspend fun getCryptoCurrency(
        @Path("id") id: String,
        @Query("vs_currency") fiatCurrency: String,
        @Query("days") daysRange: String,
        @Query("interval") interval: String
    ): Response<CryptoResponse>
}