package com.example.crypto_analytics.ui.viewmodel

import android.app.Application
import android.hardware.fingerprint.FingerprintManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.crypto_analytics.data.api.CryptoAPI
import com.example.crypto_analytics.data.model.CryptoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GraphViewModel(application: Application): AndroidViewModel(application) {

    val cryptoLiveData = MutableLiveData<CryptoResponse>()

    fun getCryptoCurrency(cryptoAPI: CryptoAPI) {
        viewModelScope.launch(Dispatchers.IO) {
            cryptoAPI.getCryptoCurrency()
        }
    }
}