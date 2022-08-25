package com.example.crypto_analytics.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.crypto_analytics.data.model.NewsData

@Dao
interface NewsBaseDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(newsData: NewsData)

    @Delete
    fun deleteItem(newsData: NewsData)

    @Query("SELECT * FROM news")
    fun getAll(): MutableList<NewsData>

}