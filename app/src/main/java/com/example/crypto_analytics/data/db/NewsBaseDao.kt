package com.example.crypto_analytics.data.db

import androidx.room.*
import com.example.crypto_analytics.data.model.NewsData
import kotlinx.coroutines.flow.Flow
import java.util.ArrayList

@Dao
interface NewsBaseDao {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(newsData: NewsData)

    @Delete
    suspend fun deleteItem(newsData: NewsData)

    @Query("SELECT * FROM news")
    fun getAll(): Flow<List<NewsData>>

}