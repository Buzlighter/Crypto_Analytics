package com.example.crypto_analytics.ui.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    list: ArrayList<Fragment>,
    fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val listOfFragment = list

    override fun getItemCount(): Int {
       return  listOfFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return listOfFragment[position]
    }
}