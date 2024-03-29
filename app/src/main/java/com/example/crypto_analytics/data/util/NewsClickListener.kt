package com.example.crypto_analytics.data.util

import com.example.crypto_analytics.data.model.NewsData
import com.example.crypto_analytics.databinding.NewsHolderBinding

interface NewsClickListener{
    fun onClick(newsItem: NewsData, position: Int)
    fun onLongClick(newsHolderBinding: NewsHolderBinding, newsData: NewsData, position: Int)
}