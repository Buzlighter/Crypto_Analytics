package com.example.crypto_analytics.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsSource(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?): Parcelable
