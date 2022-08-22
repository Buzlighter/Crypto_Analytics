package com.example.crypto_analytics.data.util

import android.content.Context
import androidx.fragment.app.Fragment
import com.example.crypto_analytics.App
import com.example.crypto_analytics.di.components.AppComponent

val Context.appComponent: AppComponent
    get() = when(this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }

val Fragment.appComponent: AppComponent
get() = requireActivity().appComponent
