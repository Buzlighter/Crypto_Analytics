package com.example.crypto_analytics.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrenciesSparklineDao {

//    @Insert (onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(currency: Currency)
//
//    @Query("SELECT * FROM currencies WHERE currency = :currencyName")
//    fun getCurrencyByName(currencyName: String): Currency
//
//    @Query("SELECT * FROM currencies")
//    fun getAll(): LiveData<List<Currency>>

}