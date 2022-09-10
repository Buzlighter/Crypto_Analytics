package com.example.crypto_analytics.di.modules

import android.app.Application
import androidx.room.Room
import com.example.crypto_analytics.data.db.CryptoDataBase
import com.example.crypto_analytics.data.db.NewsBaseDao
import com.example.crypto_analytics.data.util.Constants
import com.example.crypto_analytics.data.util.Converters
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataBaseModule {

    @Provides
    @Singleton
    fun provideDataBaseClient(application: Application) : CryptoDataBase {
        return  Room.databaseBuilder(
            application,
            CryptoDataBase::class.java,
            Constants.DB_NAME)
            .addTypeConverter(Converters())
            .build()
    }

    @Provides
    @Singleton
    fun provideSparkLineDao(cryptoDataBase: CryptoDataBase): NewsBaseDao {
        return cryptoDataBase.newsBaseDao()
    }
}