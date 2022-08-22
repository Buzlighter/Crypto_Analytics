package com.example.crypto_analytics.di.modules

import android.app.Application
import androidx.room.Room
import com.example.crypto_analytics.data.db.CurrenciesSparklineDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

//@Module
//class DataBaseModule {
//
//    @Provides
//    @Singleton
//    fun provideDataBaseClient(application: Application) : CryptoDataBase {
//        return  Room.databaseBuilder(
//            application,
//            CryptoDataBase::class.java,
//            "crypto_db"
//        ).build()
//    }
//
//    @Provides
//    @Singleton
//    fun provideSparkLineDao(cryptoDataBase: CryptoDataBase): CurrenciesSparklineDao {
//        return cryptoDataBase.CurrenciesSparklineDao()
//    }
//}