package com.example.crypto_analytics.ui.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager,
                       lifecycle: Lifecycle,
                       var listOfFragments: List<Fragment>): FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {

        return when(position) {
            0 -> listOfFragments[position]
            1 -> listOfFragments[position]
            else -> listOfFragments[position]
        }
    }

    override fun getItemCount(): Int {
        return listOfFragments.size
    }

}