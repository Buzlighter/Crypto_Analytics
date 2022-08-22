package com.example.crypto_analytics.di.modules

import com.example.crypto_analytics.data.api.CryptoService
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
class CryptoNetworkModule {

    @Singleton
    @Provides
    @Named("crypto")
    fun provideHttpOkClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()
    }

    @Singleton
    @Provides
    @Named("crypto")
    fun provideRetrofit(@Named("crypto") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constants.COINGECKO_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideCryptoService(@Named("crypto") retrofit: Retrofit): CryptoService = retrofit.create(CryptoService::class.java)

}