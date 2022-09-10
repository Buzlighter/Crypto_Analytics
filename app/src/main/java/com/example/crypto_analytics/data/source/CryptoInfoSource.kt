package com.example.crypto_analytics.data.source

import com.example.crypto_analytics.data.api.CoinMapService
import com.example.crypto_analytics.data.model.ATM
import com.example.crypto_analytics.data.model.CoinMapData
import com.example.crypto_analytics.data.util.DataState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CryptoInfoSource @Inject constructor(private val coinMapService: CoinMapService) {

    val mapValueData: Flow<DataState<List<ATM>>> = flow {
        emit(DataState.Loading())
        val response = coinMapService.getCoordinates()
        if (response.isSuccessful) {
            emit(DataState.Success(response.body()?.listOfATM))
        }
        else {
            emit(DataState.Error(null))
        }
    }
}