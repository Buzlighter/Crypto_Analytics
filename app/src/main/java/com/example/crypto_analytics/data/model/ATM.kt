package com.example.crypto_analytics.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ATM(
    @SerializedName("id")
    val id: Int,
    @SerializedName("lat")
    val latitude: Float,
    @SerializedName("lon")
    val lontitude: Float,
    @SerializedName("category")
    val category: String,
    @SerializedName("name")
    val name: String
): Parcelable
