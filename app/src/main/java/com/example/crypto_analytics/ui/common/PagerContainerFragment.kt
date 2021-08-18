package com.example.crypto_analytics.ui.common

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.*
import androidx.viewpager2.widget.ViewPager2
import com.example.crypto_analytics.R
import com.example.crypto_analytics.ui.view.graph_screen.GraphFragment
import com.example.crypto_analytics.ui.view.info_screen.InfoFragment
import com.example.crypto_analytics.ui.view.news_screen.NewsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class PagerContainerFragment : Fragment(R.layout.fragment_pager_container) {
    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavView: BottomNavigationView

    private val fragmentList = arrayListOf(
        GraphFragment.newInstance(),
        NewsFragment.newInstance(),
        InfoFragment.newInstance()
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavView = view.findViewById(R.id.bottom_nav_view)
        viewPager = view.findViewById(R.id.view_pager)

        val adapter = ViewPagerAdapter(fragmentList, childFragmentManager, lifecycle)
        viewPager.adapter = adapter

        bottomNavView.setOnItemSelectedListener(itemSelectedListener)
        viewPager.registerOnPageChangeCallback(pageListener)

        //Handle Back Pressed
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true, handleOnBackPressed)
    }

    private val itemSelectedListener = NavigationBarView.OnItemSelectedListener { item ->
        when(item.itemId) {
            R.id.graphFragment -> {
                viewPager.currentItem = 0
                return@OnItemSelectedListener true
            }
            R.id.newsFragment -> {
                viewPager.currentItem = 1
                return@OnItemSelectedListener true
            }
            R.id.infoFragment -> {
                viewPager.currentItem = 2
                return@OnItemSelectedListener true
            }
            else -> false
        }
    }

    private val pageListener = object: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            bottomNavView.menu.getItem(position).isChecked = true
        }
    }

    private val handleOnBackPressed =  fun OnBackPressedCallback.() {
        val previousItem = viewPager.currentItem - 1
        if (previousItem >= 0) {
            viewPager.currentItem = previousItem
        } else {
            this.remove()
            requireActivity().onBackPressed()
        }
    }
}