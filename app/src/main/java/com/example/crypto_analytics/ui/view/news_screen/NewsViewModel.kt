package com.example.crypto_analytics.ui.view.news_screen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crypto_analytics.data.api.NewsDataService
import com.example.crypto_analytics.data.db.NewsBaseDao
import com.example.crypto_analytics.data.model.NewsData
import com.example.crypto_analytics.data.model.NewsResponse
import com.example.crypto_analytics.data.util.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsDataService: NewsDataService, val newsBaseDao: NewsBaseDao): ViewModel() {
    val liveData = MutableLiveData<Response<NewsResponse>>()

    val dbLiveData = MutableLiveData<MutableList<NewsData>>()

    init {
        getNewsList(keyword = "биткоин")
    }

    fun getNewsList(keyword: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            liveData.postValue(newsDataService.getNews(keyword, Constants.LANGUAGE_NEWS_QUERY, Constants.NEWS_KEY))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun insertNewsData(newsData: NewsData) = viewModelScope.launch(Dispatchers.IO) {
        try {
           newsBaseDao.insert(newsData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getFavoriteNewsFromDB() = viewModelScope.launch(Dispatchers.IO) {
        try {
            dbLiveData.postValue(newsBaseDao.getAll())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteNewsItem(newsData: NewsData) = viewModelScope.launch(Dispatchers.IO) {
        try {
            newsBaseDao.deleteItem(newsData)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}