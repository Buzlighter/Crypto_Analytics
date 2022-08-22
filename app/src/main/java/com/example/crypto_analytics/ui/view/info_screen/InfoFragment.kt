package com.example.crypto_analytics.ui.view.info_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.crypto_analytics.R
import com.example.crypto_analytics.databinding.FragmentInfoBinding
import com.example.crypto_analytics.ui.view.MainActivity

class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

//    lateinit var infoFragment: InfoFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
       _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).binding.mainToolbar.title = resources.getString(R.string.info_screen_name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}