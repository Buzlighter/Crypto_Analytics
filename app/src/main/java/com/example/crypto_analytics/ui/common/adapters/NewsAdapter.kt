package com.example.crypto_analytics.ui.common.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.crypto_analytics.data.model.NewsData
import com.example.crypto_analytics.data.util.NewsClickListener
import com.example.crypto_analytics.databinding.NewsHolderBinding
import com.example.crypto_analytics.ui.common.holders.NewsHolder

class NewsAdapter(private val newsClickListener: NewsClickListener, val context: Context): RecyclerView.Adapter<NewsHolder>() {

    private val differCallBack = object: DiffUtil.ItemCallback<NewsData>() {
        override fun areItemsTheSame(oldItem: NewsData, newItem: NewsData): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: NewsData, newItem: NewsData): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val holderBinding = NewsHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsHolder(holderBinding)
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val newsItem = differ.currentList[position]
        holder.bind(newsItem, newsClickListener, position, context)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}