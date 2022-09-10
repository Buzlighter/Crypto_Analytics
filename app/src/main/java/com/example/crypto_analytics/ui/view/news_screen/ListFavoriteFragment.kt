package com.example.crypto_analytics.ui.view.news_screen

import android.animation.Animator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crypto_analytics.R
import com.example.crypto_analytics.data.model.NewsData
import com.example.crypto_analytics.data.util.Constants
import com.example.crypto_analytics.data.util.NewsClickListener
import com.example.crypto_analytics.data.util.customDeleteAnimation
import com.example.crypto_analytics.databinding.FragmentListFavoriteBinding
import com.example.crypto_analytics.databinding.NewsHolderBinding
import com.example.crypto_analytics.ui.common.adapters.NewsAdapter
import kotlinx.coroutines.launch

class ListFavoriteFragment : Fragment() {
    private var _binidng: FragmentListFavoriteBinding? = null
    private val binding get() = _binidng!!

    private lateinit var favoriteAdapter: NewsAdapter
    var favoriteList = arrayListOf<NewsData>()
    val deletedItemsList = arrayListOf<NewsData>()

    private val newsViewModel: NewsViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private var lastVisiblePosition = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binidng = FragmentListFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v("newsModel", newsViewModel.hashCode().toString())

        setFavoriteRecycler()

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectFavoriteListFromDB()
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectFilterData()
            }
        }
    }

    private suspend fun collectFilterData() {
        newsViewModel.filterStateFlow.collect { filterItem ->
            when(filterItem) {
                resources.getStringArray(R.array.filter_arr)[0] -> {
                    favoriteList.sortBy {
                        it.publishedDate
                    }
                }
                resources.getStringArray(R.array.filter_arr)[1] -> {
                    favoriteList.sortBy {
                        it.author
                    }
                }
                resources.getStringArray(R.array.filter_arr)[2] -> {
                    favoriteList.sortBy {
                        it.publishedDate
                    }
                }
                resources.getStringArray(R.array.filter_arr)[3] -> {
                    favoriteList.sortBy {
                        it.title
                    }
                }
                resources.getStringArray(R.array.filter_arr)[4] -> {
                    favoriteList.sortBy {
                        it.description?.length
                    }
                }
            }
            if (favoriteList.size > 0) {
                favoriteAdapter.differ.submitList(favoriteList)
                favoriteAdapter.notifyItemRangeChanged(0, favoriteList.size)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binidng = null
    }

    private suspend fun collectFavoriteListFromDB() {
        newsViewModel.getFavoriteNewsFromDB().collect { dbList ->
            favoriteList = dbList as ArrayList<NewsData>
            favoriteAdapter.differ.submitList(favoriteList)
        }
    }

    private fun setFavoriteRecycler() {
        binding.favoriteRecycler.apply {
            favoriteAdapter = NewsAdapter(favoriteClickListener, requireContext())
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onPause() {
        super.onPause()
        deletedItemsList.forEach { item->
            newsViewModel.deleteNewsItem(item)
        }
    }

    override fun onStop() {
        super.onStop()
        lastVisiblePosition = (binding.favoriteRecycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        newsViewModel.storeRecyclerPosition(lastVisiblePosition)
    }

    override fun onResume() {
        super.onResume()
        newsViewModel.recyclerPositionLiveData.observe(viewLifecycleOwner) { lastPosition ->
            (binding.favoriteRecycler.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(lastPosition,0)
        }
    }

    // TODO Разобраться почему при удаление с бд recyclerview поднимается наверх

    private val favoriteClickListener = object: NewsClickListener {
        override fun onClick(newsItem: NewsData, position: Int) {
            val detailsBundle = bundleOf(Constants.NEWS_TO_DETAILS_BUNDLE_KEY to newsItem)
            activity?.findNavController(R.id.nav_host)?.navigate(R.id.action_pagerContainerFragment_to_detailsFragment, detailsBundle)
        }

        override fun onLongClick(newsHolderBinding: NewsHolderBinding, newsData: NewsData, position: Int) {
            customDeleteAnimation(newsHolderBinding.root, requireContext(), 400)
            favoriteAdapter.notifyItemRemoved(position)
            favoriteAdapter.notifyItemRangeChanged(0, favoriteAdapter.itemCount)

            favoriteList.remove(newsData)
            deletedItemsList.add(newsData)

            binding.deleteAnimation.apply {
                binding.deleteAnimation.visibility = View.VISIBLE
                addAnimatorListener(deleteAnimationListener)
                playAnimation()
                animate().alpha(0.7f)
            }
        }
    }


    val deleteAnimationListener = object: Animator.AnimatorListener {

        override fun onAnimationStart(animation: Animator?) {

        }

        override fun onAnimationEnd(animation: Animator?) {
            binding.deleteAnimation.visibility = View.GONE
        }

        override fun onAnimationCancel(animation: Animator?) {
            binding.deleteAnimation.visibility = View.GONE
        }

        override fun onAnimationRepeat(animation: Animator?) {
        }
    }
}