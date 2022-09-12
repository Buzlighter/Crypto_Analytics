package com.example.crypto_analytics

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.crypto_analytics.data.util.Constants
import com.example.crypto_analytics.di.components.AppComponent
import com.example.crypto_analytics.di.components.DaggerAppComponent
import com.example.crypto_analytics.di.modules.AppModule
import com.example.crypto_analytics.di.modules.CryptoNetworkModule
import com.example.crypto_analytics.di.modules.DataBaseModule
import com.example.crypto_analytics.di.modules.NewsNetworkModule

class App: Application() {

    lateinit var appComponent: AppComponent

    companion object {
        var notificationPrice = 0F
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .cryptoNetworkModule(CryptoNetworkModule())
            .newsNetworkModule(NewsNetworkModule())
            .dataBaseModule(DataBaseModule())
            .build()


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val cryptoChannel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                Constants.NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(cryptoChannel)
        }
    }
}