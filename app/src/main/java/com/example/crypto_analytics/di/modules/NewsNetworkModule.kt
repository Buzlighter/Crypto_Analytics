package com.example.crypto_analytics.di.modules

import com.example.crypto_analytics.data.api.NewsDataService
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
class NewsNetworkModule {
    @Singleton
    @Provides
    @Named("news")
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
    @Named("news")
    fun provideRetrofit(@Named("news") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constants.NEWSDATA_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsDataService(@Named("news") retrofit: Retrofit): NewsDataService = retrofit.create(NewsDataService::class.java)

}