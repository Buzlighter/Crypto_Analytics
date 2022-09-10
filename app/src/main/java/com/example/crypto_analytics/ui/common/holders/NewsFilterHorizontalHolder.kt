package com.example.crypto_analytics.ui.common.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.crypto_analytics.data.util.OnItemFilterClickListener
import com.example.crypto_analytics.databinding.NewsFilterHorizontalHolderBinding


class NewsFilterHorizontalHolder(val itemBinding: NewsFilterHorizontalHolderBinding): RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(filterItemContent: String, onItemFilterClickListener: OnItemFilterClickListener, position: Int) {
        itemBinding.itemFilterType.text = filterItemContent
        itemView.setOnClickListener {
            onItemFilterClickListener.onItemClick(position)
        }
    }
}