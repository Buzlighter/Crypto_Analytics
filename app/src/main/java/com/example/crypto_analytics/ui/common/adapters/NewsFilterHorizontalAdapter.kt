package com.example.crypto_analytics.ui.common.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.crypto_analytics.R
import com.example.crypto_analytics.data.util.OnItemFilterClickListener
import com.example.crypto_analytics.databinding.NewsFilterHorizontalHolderBinding
import com.example.crypto_analytics.ui.common.holders.NewsFilterHorizontalHolder

class NewsFilterHorizontalAdapter(
    val onItemClickListener: OnItemFilterClickListener,
    val context: Context
    ): RecyclerView.Adapter<NewsFilterHorizontalHolder>() {

    private var selectedPos = RecyclerView.NO_POSITION

    private val diffUtilCallback = object: DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtilCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsFilterHorizontalHolder {
        val itemBinding = NewsFilterHorizontalHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsFilterHorizontalHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: NewsFilterHorizontalHolder, position: Int) {
        holder.itemView.isSelected = selectedPos == position
        val filterItemContent = differ.currentList[position]
        if(selectedPos == position) {
            holder.itemView.background = AppCompatResources.getDrawable(context, R.drawable.filter_horizontal_item_enabled)
        } else {
            holder.itemView.background = AppCompatResources.getDrawable(context, R.drawable.filter_horizontal_item_disabled)
        }
        holder.bind(filterItemContent, onItemClickListener, position)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun setSelectedPosition(pos: Int) {
        selectedPos = pos
    }

}