package com.example.crypto_analytics.ui.view.home_screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crypto_analytics.data.model.CryptoEntity
import com.example.crypto_analytics.data.repository.CryptoRepositoryImpl
import com.example.crypto_analytics.data.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(private val repository: CryptoRepositoryImpl,
                    daysRange: Int,
                    cryptoCurrency: String,
                    fiatCurrency: String): ViewModel() {

    private val localCryptoLiveData = MutableLiveData<Resource<CryptoEntity>>()
    val cryptoLiveData: LiveData<Resource<CryptoEntity>> = localCryptoLiveData
    init {
        getCryptoCurrency(daysRange, cryptoCurrency, fiatCurrency)
    }

    // Сделать Repository и ViewModelProvider и передать сюда объект репозитория
    fun getCryptoCurrency(daysRange: Int, cryptoCurrency: String,
                          fiatCurrency: String) = viewModelScope.launch(Dispatchers.IO) {
        val responseData = repository.getFiatExchangeRates(daysRange.toString(), cryptoCurrency, fiatCurrency)
        localCryptoLiveData.postValue(responseData)
        Timber.d("${responseData.data}")
    }
}