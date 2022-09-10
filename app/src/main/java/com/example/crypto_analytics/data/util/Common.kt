package com.example.crypto_analytics.data.util

import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.crypto_analytics.R
import com.example.crypto_analytics.data.model.NewsData
import com.example.crypto_analytics.ui.common.PagerContainerFragment
import com.example.crypto_analytics.ui.common.adapters.NewsAdapter
import com.example.crypto_analytics.ui.view.news_screen.NewsViewModel
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job

fun customDeleteAnimation(view: View?, context: Context, duration: Long) {
    if (view != null) {
        val anim: Animation = AnimationUtils.loadAnimation(
            context,
            R.anim.slide_out_right
        )
        anim.duration = duration
        view.startAnimation(anim)
    }
}

fun getErrorSnackBar(view: View): Snackbar {
    return Snackbar.make(view, "Ошибка подключения", Snackbar.LENGTH_INDEFINITE)
        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
        .setAnchorView(view)
}

fun hasInternetConnectivity(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
            return true
        }
    }
    return false
}


