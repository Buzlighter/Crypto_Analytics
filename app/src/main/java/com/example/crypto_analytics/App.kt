package com.example.crypto_analytics

import android.app.Application
import com.example.crypto_analytics.data.db.CryptoDataBase
import com.example.crypto_analytics.data.util.Constants
import com.example.crypto_analytics.di.components.AppComponent
import com.example.crypto_analytics.di.components.DaggerAppComponent
import com.example.crypto_analytics.di.modules.AppModule
import com.example.crypto_analytics.di.modules.CryptoNetworkModule
import com.example.crypto_analytics.di.modules.DataBaseModule
import com.example.crypto_analytics.di.modules.NewsNetworkModule

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .cryptoNetworkModule(CryptoNetworkModule())
            .newsNetworkModule(NewsNetworkModule())
            .dataBaseModule(DataBaseModule())
            .build()

    }
}