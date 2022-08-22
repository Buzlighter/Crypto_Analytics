package com.example.crypto_analytics.ui.view.news_screen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crypto_analytics.data.api.NewsDataService
import com.example.crypto_analytics.data.model.NewsResponse
import com.example.crypto_analytics.data.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsDataService: NewsDataService): ViewModel() {
    val liveData = MutableLiveData<Response<NewsResponse>>()

    init {
        getNewsList(keyword = "биткоин")
    }

    fun getNewsList(keyword: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            liveData.postValue(newsDataService.getNews(keyword, Constants.LANGUAGE_NEWS_QUERY, Constants.NEWS_KEY))
        } catch (e: Exception) {
        }
    }
}