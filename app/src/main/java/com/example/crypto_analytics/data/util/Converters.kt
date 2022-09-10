package com.example.crypto_analytics.data.util

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.crypto_analytics.data.model.NewsData
import com.example.crypto_analytics.data.model.NewsSource
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Singleton
@ProvidedTypeConverter
class Converters {

    @TypeConverter
    fun fromJson(json: String): NewsSource {
        val type = object : TypeToken<NewsSource>() {}.type
        return Gson().fromJson(json, type) ?: NewsSource("", "")
    }

    @TypeConverter
    fun toJson(torrent: NewsSource): String {
        val type = object: TypeToken<NewsSource>() {}.type
        return Gson().toJson(torrent, type) ?: ""
    }
}