package com.example.crypto_analytics.ui.view.news_screen
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crypto_analytics.R
import com.example.crypto_analytics.data.model.NewsData
import com.example.crypto_analytics.data.util.Constants
import com.example.crypto_analytics.data.util.NewsClickListener
import com.example.crypto_analytics.data.util.appComponent
import com.example.crypto_analytics.databinding.FragmentNewsBinding
import com.example.crypto_analytics.ui.common.PagerContainerFragment
import com.example.crypto_analytics.ui.common.adapters.NewsAdapter
import com.example.crypto_analytics.ui.view.MainActivity
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class NewsFragment : Fragment() {
    private var _binidng: FragmentNewsBinding? = null
    private val binding get() = _binidng!!

    private lateinit var newsAdapter: NewsAdapter
    private lateinit var favoriteAdapter: NewsAdapter

    var list = listOf<NewsData>()
    var favoriteList = mutableListOf<NewsData>()

    @Inject
    lateinit var newsViewModelFactory: NewsViewModelFactory

    val newsViewModel: NewsViewModel by viewModels { newsViewModelFactory }

//    lateinit var newsFragment: NewsFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        appComponent.inject(this)
        _binidng = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNewsRecycler()
        setFavoriteRecycler()

        newsViewModel.liveData.observe(viewLifecycleOwner) { response ->
           list = response.body()?.articles!!.filter {
               it.imageUrl != null
           }
            newsAdapter.differ.submitList(list)
            Log.d("NEEWS", newsAdapter.differ.currentList.size.toString())
        }

        binding.newsSearcher.setOnQueryTextListener(onQueryListener)
        binding.toggleButtonLayout.toggleGroupButton.addOnButtonCheckedListener(groupBtnCheckedListener)

    }



    private fun setNewsRecycler() {
//        pullToRefresh.setOnRefreshListener()

        binding.newsRecycler.apply {
            newsAdapter = NewsAdapter(newsClickListener, requireContext())
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun setFavoriteRecycler() {
        binding.favoriteRecycler.apply {
            favoriteAdapter = NewsAdapter(favoriteClickListener, requireContext())
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    private val onQueryListener = object: SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            newsViewModel.getNewsList(query ?: "")
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    }

    private val groupBtnCheckedListener = MaterialButtonToggleGroup.OnButtonCheckedListener { group, checkedId, isChecked ->
        if (isChecked) {
            when (checkedId) {
                R.id.btn_all -> {
                    binding.newsRecycler.visibility = View.VISIBLE
                    binding.favoriteRecycler.visibility = View.GONE

                    newsAdapter.differ.submitList(list)
                }
                R.id.btn_favorite -> {
                    binding.newsRecycler.visibility = View.GONE
                    binding.favoriteRecycler.visibility = View.VISIBLE

                    newsViewModel.getFavoriteNewsFromDB()
                    newsViewModel.dbLiveData.observe(viewLifecycleOwner) { dbList ->
                        favoriteList = dbList
                        favoriteAdapter.differ.submitList(favoriteList)
                    }
                    Log.d("NEEWS", "favorite ${favoriteList.size}")
                }
            }
        }
    }


    override fun onResume() {
        (activity as MainActivity).binding.mainToolbar.title = resources.getString(R.string.news_screen_name)
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binidng = null
    }

    private val newsClickListener = object: NewsClickListener {
        override fun onClick(newsItem: NewsData, position: Int) {

            findNavController().apply {
                if (currentDestination == findDestination(R.id.pagerContainerFragment)) {
                    val bundle = bundleOf(Constants.NEWS_TO_DETAILS_BUNDLE_KEY to newsItem)
                    findNavController().navigate(R.id.action_pagerContainerFragment_to_detailsFragment, bundle)
                }
            }
        }

        override fun onLongClick(newsData: NewsData, position: Int) {
            newsViewModel.insertNewsData(newsData)
            Snackbar
                .make(PagerContainerFragment.bottomNavigationView,"Добавлено в избранное",Snackbar.LENGTH_SHORT)
                .setAnimationMode(ANIMATION_MODE_SLIDE)
                .setAnchorView(PagerContainerFragment.bottomNavigationView)
                .show()
        }
    }

    private val favoriteClickListener = object: NewsClickListener {
        override fun onClick(newsItem: NewsData, position: Int) {

            findNavController().apply {
                if (currentDestination == findDestination(R.id.pagerContainerFragment)) {
                    val bundle = bundleOf(Constants.NEWS_TO_DETAILS_BUNDLE_KEY to newsItem)
                    findNavController().navigate(R.id.action_pagerContainerFragment_to_detailsFragment, bundle)
                }
            }
        }

        override fun onLongClick(newsData: NewsData, position: Int) {
            favoriteList.remove(newsData)
            favoriteAdapter.notifyItemRemoved(position)
            newsViewModel.deleteNewsItem(newsData)
        }
    }
}