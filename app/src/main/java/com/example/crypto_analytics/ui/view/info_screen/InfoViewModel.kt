package com.example.crypto_analytics.ui.view.info_screen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crypto_analytics.data.model.ATM
import com.example.crypto_analytics.data.model.CoinMapData
import com.example.crypto_analytics.data.source.CryptoInfoSource
import com.example.crypto_analytics.data.util.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class InfoViewModel(private val cryptoInfoSource: CryptoInfoSource) : ViewModel() {
    private val mutableCoinMapStateFlow = MutableStateFlow<DataState<List<ATM>>>(DataState.Loading())
    val coinMapStateFlow: StateFlow<DataState<List<ATM>>> = mutableCoinMapStateFlow

    init {
        getCoinMapList()
    }

    fun getCoinMapList() = viewModelScope.launch(Dispatchers.IO) {
        cryptoInfoSource.mapValueData
            .catch {
            mutableCoinMapStateFlow.value = DataState.Error(null)
            }
            .collect{ data ->
                mutableCoinMapStateFlow.value = data
            }
    }
}