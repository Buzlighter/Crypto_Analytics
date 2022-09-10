package com.example.crypto_analytics.data.api

import com.example.crypto_analytics.data.model.ATM
import com.example.crypto_analytics.data.model.CoinMapData
import retrofit2.Response
import retrofit2.http.GET

interface CoinMapService {
    @GET("venues")
    suspend fun getCoordinates(): Response<CoinMapData>
}