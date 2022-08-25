package com.example.crypto_analytics.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.crypto_analytics.data.model.NewsData
import com.example.crypto_analytics.data.util.Converters

@Database(entities = [NewsData::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class CryptoDataBase: RoomDatabase() {

    abstract fun newsBaseDao(): NewsBaseDao
}