package com.example.crypto_analytics.data.api

import com.example.crypto_analytics.data.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsDataService {
    @GET("everything")
    suspend fun getNews(
        @Query("q") keyword: String,
        @Query("language") language: String,
        @Query("apikey") key: String
    ): Response<NewsResponse>
}