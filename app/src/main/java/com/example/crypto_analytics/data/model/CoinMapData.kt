package com.example.crypto_analytics.data.model

import com.google.gson.annotations.SerializedName

data class CoinMapData(
    @SerializedName("venues")
    val listOfATM: List<ATM>
    )