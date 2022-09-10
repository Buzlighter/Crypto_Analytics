package com.example.crypto_analytics.ui.view.news_screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.crypto_analytics.R
import com.example.crypto_analytics.data.model.NewsData
import com.example.crypto_analytics.data.util.Constants
import com.example.crypto_analytics.data.util.OnSwipeTouchListener
import com.example.crypto_analytics.databinding.FragmentDetailsBinding
import com.example.crypto_analytics.ui.view.MainActivity
import java.text.SimpleDateFormat
import java.util.*

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newsData = arguments?.getParcelable<NewsData>(Constants.NEWS_TO_DETAILS_BUNDLE_KEY)

        if (newsData != null) {
            setNewsData(newsData)
        }
    }

    private fun setNewsData(newsData: NewsData) {
        val date = newsData.publishedDate?.let { convertDateAndTimeFormat(it).split(" ")[0] }
        val time = newsData.publishedDate?.let { convertDateAndTimeFormat(it).split(" ")[1] }

        binding.apply {
            title.text = newsData.title
            content.text = newsData.description
        }

        binding.detailsPropertyLayout.apply {
            author.text = newsData.author
            publishDate.text = date
            publishedTime.text = time
            source.text = newsData.source?.name ?: "Неизвестно"
            link.text = newsData.link


            link.text = newsData.link?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
            Linkify.addLinks(link, Linkify.ALL)
            link.movementMethod = LinkMovementMethod.getInstance()
        }

        setSwipeNavigation()
    }

    override fun onResume() {
        (activity as MainActivity).binding.mainToolbar.title = resources.getString(R.string.details_screen_name)
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun convertDateAndTimeFormat(date: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return parser.parse(date)?.let { formatter.format(it) } ?: ""
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setSwipeNavigation() {
        binding.detailsLayout.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {
            override fun onSwipeRight() {
                findNavController().navigateUp()

//              (activity as MainActivity).onBackPressed()
            }
        })
    }

}