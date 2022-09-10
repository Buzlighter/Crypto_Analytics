package com.example.crypto_analytics.data.util

import android.view.View
import androidx.cardview.widget.CardView
import com.example.crypto_analytics.data.model.NewsData
import com.example.crypto_analytics.databinding.NewsHolderBinding

interface NewsClickListener{
    fun onClick(newsItem: NewsData, position: Int)
    fun onLongClick(newsHolderBinding: NewsHolderBinding, newsData: NewsData, position: Int)
}