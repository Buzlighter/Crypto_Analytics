package com.example.crypto_analytics.ui.view.info_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.crypto_analytics.data.source.CryptoInfoSource
import javax.inject.Inject

class InfoViewModelFactory @Inject constructor(private val cryptoInfoSource: CryptoInfoSource): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InfoViewModel(cryptoInfoSource) as T
    }
}