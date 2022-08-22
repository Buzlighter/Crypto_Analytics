package com.example.crypto_analytics.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NewsData(
    @SerializedName("author")
    val author: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("url")
    val link: String?,
    @SerializedName("urlToImage")
    val imageUrl: String?,
    @SerializedName("publishedAt")
    val publishedDate: String?,
    @SerializedName("content")
    val content: String?
): Serializable