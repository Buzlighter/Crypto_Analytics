package com.example.crypto_analytics.data.model

data class CryptoResponse(
    val market_caps: List<List<Double>>,
    val prices: List<List<Double>>,
    val total_volumes: List<List<Double>>)
