package com.example.crypto_analytics.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.crypto_analytics.R
import com.example.crypto_analytics.databinding.FragmentPagerContainerBinding
import com.example.crypto_analytics.ui.view.home_screen.HomeFragment
import com.example.crypto_analytics.ui.view.info_screen.InfoFragment
import com.example.crypto_analytics.ui.view.news_screen.NewsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class PagerContainerFragment : Fragment(R.layout.fragment_pager_container) {
    var _binding: FragmentPagerContainerBinding? = null
    val binding get() = _binding!!

    var bottomNavigationView: BottomNavigationView? = null
    private lateinit var viewPager: ViewPager2

    private val  listOfFragmentTabs = listOf(
        HomeFragment(),
        NewsFragment(),
        InfoFragment()
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPagerContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavigationView = view.findViewById(R.id.bottom_nav_view)
        viewPager = view.findViewById(R.id.view_pager)

        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle, listOfFragmentTabs)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(pageListener)
        bottomNavigationView?.setOnItemSelectedListener(itemSelectedListener)

        //Handle Back Pressed
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true, handleOnBackPressed)
    }


    private val handleOnBackPressed = fun OnBackPressedCallback.() {
        val previousItem = viewPager.currentItem - 1
        if (previousItem >= 0) {
            viewPager.currentItem = previousItem
        } else {
            this.remove()
            requireActivity().onBackPressed()
        }
    }
    private val itemSelectedListener = NavigationBarView.OnItemSelectedListener { item ->
        when(item.itemId) {
            R.id.menu_graphFragment -> {
                viewPager.currentItem = 0
                return@OnItemSelectedListener true
            }
            R.id.menu_newsFragment -> {
                viewPager.currentItem = 1
                return@OnItemSelectedListener true
            }
            R.id.menu_infoFragment -> {
                viewPager.currentItem = 2
                return@OnItemSelectedListener true
            }
            else -> false
        }
    }

    private val pageListener = object: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            bottomNavigationView?.menu?.getItem(position)?.isChecked = true
        }
    }
}