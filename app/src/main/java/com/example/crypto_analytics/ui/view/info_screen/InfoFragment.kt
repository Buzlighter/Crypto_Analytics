package com.example.crypto_analytics.ui.view.info_screen

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.crypto_analytics.R
import com.example.crypto_analytics.ui.view.graph_screen.GraphFragment

class InfoFragment : Fragment(R.layout.fragment_info) {
    companion object {
        fun newInstance() = InfoFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}