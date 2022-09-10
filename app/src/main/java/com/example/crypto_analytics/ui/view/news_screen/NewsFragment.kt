package com.example.crypto_analytics.ui.view.news_screen
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.crypto_analytics.R
import com.example.crypto_analytics.data.model.NewsData
import com.example.crypto_analytics.data.util.Constants
import com.example.crypto_analytics.data.util.OnItemFilterClickListener
import com.example.crypto_analytics.data.util.appComponent
import com.example.crypto_analytics.databinding.FragmentNewsBinding
import com.example.crypto_analytics.ui.common.adapters.NewsFilterHorizontalAdapter
import com.example.crypto_analytics.ui.view.MainActivity
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject


class NewsFragment : Fragment() {
    private var _binidng: FragmentNewsBinding? = null
    private val binding get() = _binidng!!

    private var keyQuery = Constants.REGULAR_NEWS_KEY_QUERY

    private lateinit var filterAdapter: NewsFilterHorizontalAdapter

    private val filterList by lazy {
        resources.getStringArray(R.array.filter_arr)
    }
    private val currentFragment by lazy {
        childFragmentManager.findFragmentById(R.id.news_container)
    }

    @Inject
    lateinit var newsViewModelFactory: NewsViewModelFactory

    private val newsViewModel: NewsViewModel by viewModels {
        newsViewModelFactory
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        appComponent.inject(this)
        _binidng = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.v("newsModel", newsViewModel.hashCode().toString())

        childFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.news_container, ListNewsFragment())
            addToBackStack(null)
        }

        setFilterRecycler()

        binding.apply {
            newsSearcher.setOnQueryTextListener(onQueryListener)
            toggleButtonLayout.toggleGroupButton.addOnButtonCheckedListener(groupBtnCheckedListener)
            newsRefresher.setOnRefreshListener(refreshListener)
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

    private fun setFilterRecycler() {
        binding.horizontalFilterLayout.filterRecycler.apply {
            filterAdapter = NewsFilterHorizontalAdapter(onFilterItemListener, requireContext())
            adapter = filterAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
        filterAdapter.differ.submitList(filterList.toMutableList())
    }

    private val refreshListener = SwipeRefreshLayout.OnRefreshListener {
        if (binding.toggleButtonLayout.toggleGroupButton.checkedButtonId == R.id.btn_all) {
            newsViewModel.getNewsList(keyQuery)
        }
        binding.newsRefresher.isRefreshing = false
    }

    private val onQueryListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            handleSearchRequest(query)
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            handleSearchRequest(newText)
            return true
        }
    }

    private fun handleSearchRequest(query: String?) {
        if (currentFragment is ListNewsFragment) {
            keyQuery = if (query.isNullOrEmpty()) {
                Constants.REGULAR_NEWS_KEY_QUERY
            } else {
                query
            }
            newsViewModel.getNewsList(keyQuery)
        }
    }

    private val groupBtnCheckedListener = MaterialButtonToggleGroup.OnButtonCheckedListener { group, checkedId, isChecked ->
        if (isChecked) {
            when (checkedId) {
                R.id.btn_all -> {
                    childFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.news_container, ListNewsFragment())
                        addToBackStack(null)
                    }
                }
                R.id.btn_favorite -> {
                    childFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace(R.id.news_container, ListFavoriteFragment())
                        addToBackStack(null)
                    }
                }
            }
        }
    }

    var selectedPos = RecyclerView.NO_POSITION
    private val onFilterItemListener = object: OnItemFilterClickListener {
        override fun onItemClick(position: Int) {
            selectedPos = position
            filterAdapter.setSelectedPosition(selectedPos)
            binding.horizontalFilterLayout.filterRecycler.adapter?.notifyDataSetChanged()

            newsViewModel.setFilterParameter(filterList[position])
        }
    }
}