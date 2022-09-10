package com.example.crypto_analytics.ui.view.home_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crypto_analytics.data.model.CryptoGraphData
import com.example.crypto_analytics.data.source.CryptoHomeSource
import com.example.crypto_analytics.data.util.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val homeSource: CryptoHomeSource,
                    daysRange: Int,
                    cryptoCurrency: String,
                    fiatCurrency: String): ViewModel() {

    private val mutableGraphDataState: MutableStateFlow<DataState<CryptoGraphData>> = MutableStateFlow(DataState.Loading())
    val cryptoGraphDataState: StateFlow<DataState<CryptoGraphData>> = mutableGraphDataState

    init {
        getCryptoCurrency(daysRange, cryptoCurrency, fiatCurrency)
    }

    fun getCryptoCurrency(days: Int, currency: String, fiat: String) = viewModelScope.launch(Dispatchers.IO) {
        homeSource.apply {
            daysRange = days.toString()
            cryptoCurrency = currency
            fiatCurrency = fiat
        }
        viewModelScope.launch {
            homeSource.cryptoGraphData
                .catch {
                    mutableGraphDataState.value = DataState.Error(null)
                    Log.d("staate", mutableGraphDataState.value.toString())
                }
                .collect { data ->
                mutableGraphDataState.value = data }
        }
    }
}