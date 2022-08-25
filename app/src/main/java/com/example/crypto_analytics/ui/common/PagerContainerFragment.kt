package com.example.crypto_analytics.ui.common

import android.os.Bundle
import android.util.Log
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

class PagerContainerFragment : Fragment() {
    var _binding: FragmentPagerContainerBinding? = null
    val binding get() = _binding!!

    companion object {
       lateinit var bottomNavigationView: BottomNavigationView
    }

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

        val adapter = ViewPagerAdapter(childFragmentManager, lifecycle, listOfFragmentTabs)
        binding.apply {
            viewPager.adapter = adapter
            viewPager.registerOnPageChangeCallback(pageListener)
            bottomNavView.setOnItemSelectedListener(itemSelectedListener)
        }

        bottomNavigationView = binding.bottomNavView

        //Handle Back Pressed
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, true, handleOnBackPressed)
    }


    private val handleOnBackPressed = fun OnBackPressedCallback.() {
        Log.d("TEEST","CONTAINER----${parentFragmentManager.findFragmentById(R.id.nav_host)?.childFragmentManager?.fragments}")
        val previousItem = binding.viewPager.currentItem - 1
        if (previousItem >= 0) {
            binding.viewPager.currentItem = previousItem
        } else {
            this.remove()
            requireActivity().onBackPressed()
        }
    }
    private val itemSelectedListener = NavigationBarView.OnItemSelectedListener { item ->
        when(item.itemId) {
            R.id.menu_graphFragment -> {
                binding.viewPager.currentItem = 0
                return@OnItemSelectedListener true
            }
            R.id.menu_newsFragment -> {
                binding.viewPager.currentItem = 1
                return@OnItemSelectedListener true
            }
            R.id.menu_infoFragment -> {
                binding.viewPager.currentItem = 2
                return@OnItemSelectedListener true
            }
            else -> false
        }
    }

    private val pageListener = object: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            binding.bottomNavView.menu.getItem(position)?.isChecked = true
        }
    }
}