package com.example.crypto_analytics.data.util

import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.crypto_analytics.R

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