package com.example.crypto_analytics.ui.common.holders

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.crypto_analytics.R
import com.example.crypto_analytics.data.model.NewsData
import com.example.crypto_analytics.data.util.NewsClickListener
import com.example.crypto_analytics.databinding.NewsHolderBinding

class NewsHolder(val binding: NewsHolderBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(newsItem: NewsData, newsClickListener: NewsClickListener, position: Int, context: Context) {
        binding.apply {
            title.text = newsItem.title
            Glide
                .with(root)
                .load(newsItem.imageUrl)
                .placeholder(R.drawable.fail_to_load_img)
                .centerCrop()
                .error(R.drawable.fail_to_load_img)
                .into(imagePoster)

            author.text = newsItem.author ?: context.getText(R.string.unknown_author)
//            date.text = newsItem.publishedDate
//            description.text = newsItem.description
            root.apply {
                setOnClickListener {
                    newsClickListener.onClick(newsItem, position)
                }

                setOnLongClickListener {
                    newsClickListener.onLongClick(newsItem, position)
                    return@setOnLongClickListener true
                }
            }

        }
    }
}