package com.example.crypto_analytics.di.modules

import com.example.crypto_analytics.data.api.CoinMapService
import com.example.crypto_analytics.data.util.Constants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class CoinMapModule {

    @Singleton
    @Provides
    @Named("map")
    fun provideHttpOkClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }

    @Singleton
    @Provides
    @Named("map")
    fun provideRetrofit(@Named("map") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constants.COINMAP_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideCoinMapService(@Named("map") retrofit: Retrofit): CoinMapService = retrofit.create(CoinMapService::class.java)
}