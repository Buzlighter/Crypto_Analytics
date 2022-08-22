package com.example.crypto_analytics.ui.view.news_screen

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import com.example.crypto_analytics.R
import com.example.crypto_analytics.data.model.NewsData
import com.example.crypto_analytics.data.util.Constants
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
        val newsData = arguments?.getSerializable(Constants.NEWS_TO_DETAILS_BUNDLE_KEY) as NewsData


        setNewsData(newsData)

    }


    private fun setNewsData(newsData: NewsData) {
        binding.apply {
            title.text = newsData.title
            content.text = newsData.description
            author.text = newsData.author
            publishDate.text = newsData.publishedDate?.let { convertDateFormat(it) }
            link.text = newsData.link

            link.text = newsData.link?.let { HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY) }
            Linkify.addLinks(link, Linkify.ALL)
            link.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    fun convertDateFormat(date: String?): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        return parser.parse(date)?.let { formatter.format(it) } ?: ""
    }

    override fun onResume() {
        (activity as MainActivity).binding.mainToolbar.title = resources.getString(R.string.details_screen_name)
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}