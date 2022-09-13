package com.example.crypto_analytics.data.model


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.crypto_analytics.data.util.Constants
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = Constants.DB_TABLE_NAME_NEWS,
    indices = [Index(value = ["author"], unique = true),
        Index(value = ["title"], unique = true),
        Index(value = ["description"], unique = true),
        Index(value = ["url"], unique = true),
        Index(value = ["urlToImage"], unique = true),
        Index(value = ["publishedAt"], unique = true),
        Index(value = ["content"], unique = true)])
@Parcelize
data class NewsData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @SerializedName("source")
    @ColumnInfo(name = "source")
    val source: NewsSource?,
    @SerializedName("author")
    @ColumnInfo(name = "author")
    val author: String?,
    @SerializedName("title")
    @ColumnInfo(name = "title")
    val title: String?,
    @SerializedName("description")
    @ColumnInfo(name = "description")
    val description: String?,
    @SerializedName("url")
    @ColumnInfo(name = "url")
    val link: String?,
    @SerializedName("urlToImage")
    @ColumnInfo(name = "urlToImage")
    val imageUrl: String?,
    @SerializedName("publishedAt")
    @ColumnInfo(name = "publishedAt")
    val publishedDate: String?,
    @SerializedName("content")
    @ColumnInfo(name = "content")
    val content: String?
): Parcelable
