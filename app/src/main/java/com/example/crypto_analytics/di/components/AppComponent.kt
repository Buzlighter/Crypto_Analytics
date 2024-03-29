package com.example.crypto_analytics.di.components

import com.example.crypto_analytics.App
import com.example.crypto_analytics.data.api.CryptoService
import com.example.crypto_analytics.di.modules.*
import com.example.crypto_analytics.ui.view.home_screen.HomeFragment
import com.example.crypto_analytics.ui.view.info_screen.InfoFragment
import com.example.crypto_analytics.ui.view.info_screen.MapsFragment
import com.example.crypto_analytics.ui.view.news_screen.ListNewsFragment
import com.example.crypto_analytics.ui.view.news_screen.NewsFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, CryptoNetworkModule::class, NewsNetworkModule::class, CoinMapModule::class, DataBaseModule::class])
interface AppComponent {

    fun inject(app: App)

    fun inject(homeFragment: HomeFragment)

    fun inject(infoFragment: InfoFragment)

    fun inject(newsFragment: NewsFragment)

    fun inject(mapsFragment: MapsFragment)

    fun inject(listNewsFragment: ListNewsFragment)

    fun getCryptoService(): CryptoService

}