package com.example.crypto_analytics.data.util

import com.example.crypto_analytics.data.model.NewsData

interface NewsClickListener {
    fun onClick(newsItem: NewsData, position: Int)
    fun onLongClick(position: Int)
}