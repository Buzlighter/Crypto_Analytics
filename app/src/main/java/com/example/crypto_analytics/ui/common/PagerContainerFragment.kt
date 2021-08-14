package com.example.crypto_analytics.ui.common

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager.widget.ViewPager
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
        GraphFragment(),
        NewsFragment(),
        InfoFragment()
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomNavView = view.findViewById(R.id.bottom_nav_view)
        viewPager = view.findViewById(R.id.view_pager)

        val adapter = ViewPagerAdapter(fragmentList, requireActivity())
        viewPager.adapter = adapter

        bottomNavView.setOnItemSelectedListener(itemSelectedListener)
        viewPager.registerOnPageChangeCallback(pageListener)
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
}