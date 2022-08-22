package com.example.crypto_analytics.data.model

data class NewsResponse(
    val status: String,
    val totalResults: String,
    val articles: List<NewsData>
)
