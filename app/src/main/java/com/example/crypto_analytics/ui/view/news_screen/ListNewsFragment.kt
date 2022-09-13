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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crypto_analytics.R
import com.example.crypto_analytics.data.model.NewsData
import com.example.crypto_analytics.data.util.*
import com.example.crypto_analytics.databinding.FragmentListNewsBinding
import com.example.crypto_analytics.databinding.NewsHolderBinding
import com.example.crypto_analytics.ui.common.PagerContainerFragment
import com.example.crypto_analytics.ui.common.adapters.NewsAdapter
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ListNewsFragment : Fragment() {
    private var _binidng: FragmentListNewsBinding? = null
    private val binding get() = _binidng!!

    private lateinit var newsAdapter: NewsAdapter
    private var newsList = arrayListOf<NewsData>()

    private val newsViewModel: NewsViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private var isValidRequest = true
    private var keyQuery = Constants.REGULAR_NEWS_KEY_QUERY

    private var snackBarError: Snackbar? = null
    private var snackBarAdd: Snackbar? = null
    private var lastVisiblePosition = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binidng = FragmentListNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v("newsModel", newsViewModel.hashCode().toString())

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                setNewsRecycler()
                collectNews()
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectFilterData()
            }
        }

        initAddSnackBar()
    }

    override fun onPause() {
        super.onPause()
        if (snackBarError != null) {
            snackBarError?.dismiss()
        }
        if (snackBarAdd != null) {
            snackBarAdd?.dismiss()
        }
    }

    override fun onStop() {
        super.onStop()
        lastVisiblePosition = (binding.newsRecycler.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        newsViewModel.storeRecyclerPosition(lastVisiblePosition)
    }

    override fun onResume() {
        super.onResume()
        newsViewModel.recyclerPositionLiveData.observe(viewLifecycleOwner) { lastPosition ->
            (binding.newsRecycler.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(lastPosition,0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binidng = null
    }


    private suspend fun collectNews() {
        newsViewModel.newsDataState.collect { state ->
            when(state) {
                is DataState.Success -> {
                    viewStateSuccess()
                    newsList = state.data!!.articles as ArrayList<NewsData>
                    newsAdapter.differ.submitList(newsList)

                    isValidRequest = state.data.articles.isEmpty()
                    if (isValidRequest) {
                        viewStateNothingFound()
                    }
                }
                is DataState.Loading -> {
                    viewStateLoading()
                }
                is DataState.Error -> {
                    viewStateError()
                }
            }
        }
    }

    private suspend fun collectFilterData() {
        newsViewModel.filterStateFlow.collect { filterItem ->
            when (filterItem) {
                resources.getStringArray(R.array.filter_arr)[0] -> {
                    newsList.sortBy {
                        it.publishedDate
                    }
                }
                resources.getStringArray(R.array.filter_arr)[1] -> {
                    newsList.sortBy {
                        it.author
                    }
                }
                resources.getStringArray(R.array.filter_arr)[2] -> {
                    newsList.sortBy {
                        it.publishedDate
                    }
                }
                resources.getStringArray(R.array.filter_arr)[3] -> {
                    newsList.sortBy {
                        it.title
                    }
                }
                resources.getStringArray(R.array.filter_arr)[4] -> {
                    newsList.sortBy {
                        it.description?.length
                    }
                }
            }
            if (newsList.size > 0) {
                newsAdapter.differ.submitList(newsList)
                newsAdapter.notifyItemRangeChanged(0, newsList.size)
            }
        }
    }

    private fun setNewsRecycler() {
        binding.newsRecycler.apply {
            newsAdapter = NewsAdapter(newsClickListener, requireContext())
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private val newsClickListener = object : NewsClickListener {
        override fun onClick(newsItem: NewsData, position: Int) {
            val detailsBundle = bundleOf(Constants.NEWS_TO_DETAILS_BUNDLE_KEY to newsItem)
            activity?.findNavController(R.id.nav_host)?.navigate(R.id.action_pagerContainerFragment_to_detailsFragment, detailsBundle)
        }

        override fun onLongClick(newsHolderBinding: NewsHolderBinding, newsData: NewsData, position: Int) {

            newsViewModel.insertNewsData(newsData)

            binding.likeAnimation.apply {
                visibility = View.VISIBLE
                addAnimatorListener(likeAnimateListener)
                playAnimation()
            }
            snackBarAdd?.show()
        }
    }

    val likeAnimateListener = object: Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            binding.likeAnimation.visibility = View.GONE
        }

        override fun onAnimationCancel(animation: Animator?) {
            binding.likeAnimation.visibility = View.GONE
        }

        override fun onAnimationRepeat(animation: Animator?) {

        }
    }

    private fun initAddSnackBar() {
        snackBarAdd =  Snackbar.make(PagerContainerFragment.bottomNavigationView, "Добавлено в избранное", Snackbar.LENGTH_SHORT)
            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
            .setAnchorView(PagerContainerFragment.bottomNavigationView)
    }


//  ViewStates

    private fun viewStateSuccess() {
        binding.apply {
            newsRecycler.visibility = View.VISIBLE
            newsErrorLayout.root.visibility = View.GONE
            newsLoadingLayout.root.visibility = View.GONE
            pageNothingFoundLayout.root.visibility = View.GONE
        }
        if (snackBarError != null) {
            snackBarError?.dismiss()
        }
    }

    private fun viewStateLoading() {
        binding.apply {
            newsLoadingLayout.root.visibility = View.VISIBLE
            pageNothingFoundLayout.root.visibility = View.GONE
            newsRecycler.visibility = View.GONE
            newsErrorLayout.root.visibility = View.GONE
        }
    }

    private fun viewStateError() {
        binding.apply {
            newsErrorLayout.root.visibility = View.VISIBLE
            newsRecycler.visibility = View.GONE
            newsLoadingLayout.root.visibility = View.GONE
            pageNothingFoundLayout.root.visibility = View.GONE
        }

        if (hasInternetConnectivity(requireContext()).not()) {
            snackBarError = getErrorSnackBar(PagerContainerFragment.bottomNavigationView)
                .setAction(R.string.repeat) {
                    newsViewModel.getNewsList(keyQuery)
                }
            snackBarError?.show()
        }
    }

    private fun viewStateNothingFound() {
        binding.apply {
            pageNothingFoundLayout.root.visibility = View.VISIBLE
            newsRecycler.visibility = View.GONE
            newsErrorLayout.root.visibility = View.GONE
            newsLoadingLayout.root.visibility = View.GONE
        }
    }
}