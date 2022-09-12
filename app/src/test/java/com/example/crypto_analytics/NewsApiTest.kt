package com.example.crypto_analytics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.crypto_analytics.data.api.NewsDataService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class NewsApiTest {
    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var newsDataService: NewsDataService


    @Test(expected = Exception::class)
    fun `newsApi that throw exception when incorrect api input`() = runTest {
        Mockito.doThrow(Exception()).`when`(newsDataService.getNews(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
    }
}