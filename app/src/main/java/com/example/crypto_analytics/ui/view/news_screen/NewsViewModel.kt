package com.example.crypto_analytics.ui.view.news_screen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crypto_analytics.data.model.NewsData
import com.example.crypto_analytics.data.model.NewsResponse
import com.example.crypto_analytics.data.source.CryptoNewsSource
import com.example.crypto_analytics.data.util.Constants
import com.example.crypto_analytics.data.util.DataState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class NewsViewModel(private val cryptoNewsSource: CryptoNewsSource): ViewModel() {
    private val mutableNewsDataState = MutableStateFlow<DataState<NewsResponse>>(DataState.Loading())
    val newsDataState: StateFlow<DataState<NewsResponse>> = mutableNewsDataState

    private val mutableFilterStateFlow = MutableStateFlow("")
    val filterStateFlow: StateFlow<String> = mutableFilterStateFlow

    private val mutableRecyclerPositionLiveData= MutableLiveData(0)
    val recyclerPositionLiveData: LiveData<Int> = mutableRecyclerPositionLiveData

    init {
        getNewsList(Constants.REGULAR_NEWS_KEY_QUERY)
    }

    fun getNewsList(keyword: String) = viewModelScope.launch(Dispatchers.IO) {
        cryptoNewsSource.queryKeyword = keyword
        cryptoNewsSource.newsData
            .catch {
                mutableNewsDataState.value = DataState.Error(null)
            }
            .collect { data ->
                mutableNewsDataState.value = data
            }
    }

    fun insertNewsData(newsData: NewsData) = viewModelScope.launch(Dispatchers.IO) {
        try {
            cryptoNewsSource.insertNewsData(newsData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getFavoriteNewsFromDB(): Flow<List<NewsData>> {
       return  cryptoNewsSource.getFavoriteNewsFromDB()
            .catch {
                Log.v("newsViewModel", "fail to get data from db")
            }
    }

    fun deleteNewsItem(newsData: NewsData) = viewModelScope.launch(Dispatchers.IO) {
        try {
            cryptoNewsSource.deleteNewsItem(newsData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setFilterParameter(filterItem: String) {
        mutableFilterStateFlow.value = filterItem
    }

    fun storeRecyclerPosition(lastPosition: Int) {
        mutableRecyclerPositionLiveData.value = lastPosition
    }
 }