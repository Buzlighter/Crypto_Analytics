package com.example.crypto_analytics.ui.view.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.crypto_analytics.data.source.CryptoHomeSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class HomeViewModelFactory @AssistedInject constructor(
    @Assisted("daysRange") private val daysRange: Int,
    @Assisted("cryptoCurrency") private val cryptoCurrency: String,
    @Assisted("fiatCurrency") private val fiatCurrency: String,
    private val repository: CryptoHomeSource
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository, daysRange, cryptoCurrency, fiatCurrency) as T
    }

    @AssistedFactory
    interface ServiceFactory {
        fun create(@Assisted("daysRange") daysRange: Int,
                   @Assisted("cryptoCurrency") cryptoCurrency: String,
                   @Assisted("fiatCurrency")fiatCurrency: String): HomeViewModelFactory
    }
}