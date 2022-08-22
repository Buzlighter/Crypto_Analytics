package com.example.crypto_analytics.data.repository

import com.example.crypto_analytics.data.api.CryptoService
import com.example.crypto_analytics.data.model.CryptoEntity
import com.example.crypto_analytics.data.util.Resource
import javax.inject.Inject
import kotlin.math.roundToInt


class CryptoRepositoryImpl @Inject constructor(val cryptoService: CryptoService) {
    companion object {
        const val INTERVAL = "daily"
    }

    suspend fun getFiatExchangeRates(daysRange: String,
                                     cryptoCurrency: String,
                                     fiatCurrency: String): Resource<CryptoEntity> {
        val responseData = cryptoService.getCryptoCurrency(
            cryptoCurrency,
            fiatCurrency,
            daysRange,
            INTERVAL
        )

        val listOfPrice = mutableListOf<Float>()
        if (responseData.isSuccessful) {
            responseData.body()?.prices?.forEach { list ->
                list.map {
                    if (list.indexOf(it) % 2 != 0) {
                        val roundedPrice = (it * 10).roundToInt().toFloat() / 10
                        listOfPrice.add(roundedPrice)
                    }

                }
            }
        }
        return Resource.Success(CryptoEntity(listOfPrice))
    }
}