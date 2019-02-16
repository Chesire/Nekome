package com.chesire.malime.kitsu.live

import com.chesire.malime.core.Resource
import com.chesire.malime.kitsu.adapters.ImageModelAdapter
import com.chesire.malime.kitsu.api.search.SearchSeriesModelAdapter
import com.chesire.malime.kitsu.adapters.SeriesStatusAdapter
import com.chesire.malime.kitsu.adapters.SeriesTypeAdapter
import com.chesire.malime.kitsu.adapters.SubtypeAdapter
import com.chesire.malime.kitsu.api.search.KitsuSearch
import com.chesire.malime.kitsu.api.search.KitsuSearchService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Ignore("Don't run these tests as part of any suite, they are for testing the apis")
class LiveKitsuSearchTests {
    private val moshi = Moshi.Builder()
        .add(ImageModelAdapter())
        .add(SeriesStatusAdapter())
        .add(SeriesTypeAdapter())
        .add(SearchSeriesModelAdapter())
        .add(SubtypeAdapter())
        .build()

    private val httpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(LiveTestAuthInterceptor())
        .build()

    private val service = Retrofit.Builder()
        .baseUrl("https://kitsu.io/")
        .client(httpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(KitsuSearchService::class.java)

    private val handler = KitsuSearch(service)

    @Test
    fun `attempt searchForAnime`() = runBlocking {
        val job = launch {
            val result = handler.searchForAnime("No Game No Life")

            when (result) {
                is Resource.Success -> {
                    val data = result.data
                }
                is Resource.Error -> {
                    val error = result.msg
                }
            }
        }
    }

    @Test
    fun `attempt searchForManga`() = runBlocking {
        val job = launch {
            val result = handler.searchForManga("No Game No Life")

            when (result) {
                is Resource.Success -> {
                    val data = result.data
                }
                is Resource.Error -> {
                    val error = result.msg
                }
            }
        }
    }
}
