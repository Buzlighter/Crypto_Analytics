package com.example.crypto_analytics.ui.view.news_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.crypto_analytics.data.api.NewsDataService
import com.example.crypto_analytics.data.db.NewsBaseDao
import javax.inject.Inject

class NewsViewModelFactory @Inject constructor(val newsDataService: NewsDataService,
                                               val newsBaseDao: NewsBaseDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsDataService, newsBaseDao) as T
    }
}