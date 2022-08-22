package com.example.crypto_analytics

import android.app.Application
import com.example.crypto_analytics.data.util.Constants
import com.example.crypto_analytics.di.components.AppComponent
import com.example.crypto_analytics.di.components.DaggerAppComponent
import com.example.crypto_analytics.di.modules.CryptoNetworkModule
import com.example.crypto_analytics.di.modules.NewsNetworkModule
import timber.log.Timber
import timber.log.Timber.DebugTree

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .cryptoNetworkModule(CryptoNetworkModule())
            .newsNetworkModule(NewsNetworkModule())
            .build()

        if(BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}