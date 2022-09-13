package com.example.crypto_analytics.data.source

import com.example.crypto_analytics.data.api.NewsDataService
import com.example.crypto_analytics.data.db.NewsBaseDao
import com.example.crypto_analytics.data.model.NewsData
import com.example.crypto_analytics.data.model.NewsResponse
import com.example.crypto_analytics.data.util.Constants
import com.example.crypto_analytics.data.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CryptoNewsSource @Inject constructor(private val newsDataService: NewsDataService,
                                           private val newsBaseDao: NewsBaseDao) {

    var queryKeyword: String = ""

    val newsData: Flow<DataState<NewsResponse>> = flow {
       emit(DataState.Loading())
       val response = newsDataService.getNews(queryKeyword, Constants.LANGUAGE_NEWS_QUERY, Constants.NEWS_KEY)
        if (response.isSuccessful) {
            emit(DataState.Success(response.body()))
        } else {
            emit(DataState.Error(null))
        }
    }


    fun getFavoriteNewsFromDB(): Flow<List<NewsData>> =  newsBaseDao.getAll()

    suspend fun insertNewsData(newsData: NewsData) = newsBaseDao.insert(newsData)
    suspend fun deleteNewsItem(newsData: NewsData) = newsBaseDao.deleteItem(newsData)

}