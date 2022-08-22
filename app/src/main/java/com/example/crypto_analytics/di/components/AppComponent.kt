package com.example.crypto_analytics.di.components

import com.example.crypto_analytics.App
import com.example.crypto_analytics.data.api.NewsDataService
import com.example.crypto_analytics.di.modules.AppModule
import com.example.crypto_analytics.di.modules.CryptoNetworkModule
import com.example.crypto_analytics.di.modules.NewsNetworkModule
import com.example.crypto_analytics.ui.view.home_screen.HomeFragment
import com.example.crypto_analytics.ui.view.info_screen.InfoFragment
import com.example.crypto_analytics.ui.view.news_screen.NewsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, CryptoNetworkModule::class, NewsNetworkModule::class])
interface AppComponent {

    fun inject(app: App)

    fun inject(homeFragment: HomeFragment)

    fun inject(infoFragment: InfoFragment)

    fun inject(newsFragment: NewsFragment)

}