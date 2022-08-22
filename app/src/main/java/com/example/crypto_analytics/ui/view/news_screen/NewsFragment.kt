package com.example.crypto_analytics.ui.view.news_screen
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
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
import com.example.crypto_analytics.ui.common.adapters.NewsAdapter
import com.example.crypto_analytics.ui.view.MainActivity
import timber.log.Timber
import javax.inject.Inject

class NewsFragment : Fragment() {
    private var _binidng: FragmentNewsBinding? = null
    private val binding get() = _binidng!!
    private lateinit var newsAdapter: NewsAdapter

    @Inject
    lateinit var newsViewModelFactory: NewsViewModelFactory

    val newsViewModel: NewsViewModel by viewModels { newsViewModelFactory }

//    lateinit var newsFragment: NewsFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        appComponent.inject(this)
        _binidng = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }


    var list = listOf<NewsData>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setNewsRecycler()
        newsViewModel.liveData.observe(viewLifecycleOwner) {response ->
           list = response.body()?.articles!!.filter {
               it.imageUrl != null
           }
            list.forEach {
                Timber.d("IMAGE -->>> ${it.imageUrl}\n")
            }
            newsAdapter.differ.submitList(list)
        }

        binding.newsSearcher.setOnQueryTextListener(onQueryListener)
    }

    private fun setNewsRecycler() {
//        pullToRefresh.setOnRefreshListener()

        binding.newsRecycler.apply {
            newsAdapter = NewsAdapter(newsClickListener, requireContext())
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = newsAdapter
            setHasFixedSize(true)
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

        override fun onLongClick(position: Int) {
            TODO("Not yet implemented")
        }

    }
}