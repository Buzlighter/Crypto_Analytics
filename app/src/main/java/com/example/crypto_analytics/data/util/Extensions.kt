package com.example.crypto_analytics.data.util

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.crypto_analytics.App
import com.example.crypto_analytics.di.components.AppComponent
import com.example.crypto_analytics.ui.view.news_screen.ListNewsFragment
import com.example.crypto_analytics.ui.view.news_screen.NewsViewModel
import com.example.crypto_analytics.ui.view.news_screen.NewsViewModelFactory
import dagger.internal.Factory

val Context.appComponent: AppComponent
    get() = when(this) {
        is App -> appComponent
        else -> this.applicationContext.appComponent
    }

val Fragment.appComponent: AppComponent
get() = requireActivity().appComponent
