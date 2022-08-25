package com.example.crypto_analytics.data.util

import android.view.View
import com.example.crypto_analytics.data.model.NewsData

interface NewsClickListener{
    fun onClick(newsItem: NewsData, position: Int)
    fun onLongClick(newsData: NewsData, position: Int)
}