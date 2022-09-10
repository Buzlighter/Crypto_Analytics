package com.example.crypto_analytics.data.source

import com.example.crypto_analytics.data.api.CryptoService
import com.example.crypto_analytics.data.model.CryptoGraphData
import com.example.crypto_analytics.data.util.DataState
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.math.roundToInt


class CryptoHomeSource @Inject constructor(private val cryptoService: CryptoService) {

    var cryptoCurrency = ""
    var fiatCurrency = ""
    var daysRange = ""
    private val interval = "daily"


    val cryptoGraphData: Flow<DataState<CryptoGraphData>> = flow {
        emit(DataState.Loading())
        val responseData = cryptoService.getCryptoCurrency(cryptoCurrency, fiatCurrency, daysRange, interval)
        if (responseData.isSuccessful) {
            val listOfPrice = mutableListOf<Float>()
            responseData.body()?.prices?.forEach { list ->
                list.map {
                    if (list.indexOf(it) % 2 != 0) {
                        val roundedPrice = (it * 10).roundToInt().toFloat() / 10
                        listOfPrice.add(roundedPrice)
                    }
                }
            }
            emit(DataState.Success(CryptoGraphData(listOfPrice)))
        } else {
            emit(DataState.Error(null))
        }
    }

}