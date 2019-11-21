package com.chesire.malime.kitsu.live

import com.chesire.malime.kitsu.adapters.ImageModelAdapter
import com.chesire.malime.kitsu.adapters.SeriesStatusAdapter
import com.chesire.malime.kitsu.adapters.SeriesTypeAdapter
import com.chesire.malime.kitsu.adapters.SubtypeAdapter
import com.chesire.malime.kitsu.api.trending.KitsuTrending
import com.chesire.malime.kitsu.api.trending.KitsuTrendingService
import com.chesire.malime.kitsu.api.trending.TrendingAdapter
import com.chesire.malime.server.Resource
import com.squareup.moshi.Moshi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Ignore("Don't run these tests as part of any suite, they are for testing the apis")
class LiveKitsuTrendingTests {
    private val moshi = Moshi.Builder()
        .add(ImageModelAdapter())
        .add(SeriesStatusAdapter())
        .add(SeriesTypeAdapter())
        .add(TrendingAdapter())
        .add(SubtypeAdapter())
        .build()

    private val httpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(LiveTestAuthInterceptor())
        .build()

    private val service = Retrofit.Builder()
        .baseUrl("https://kitsu.io/")
        .client(httpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(KitsuTrendingService::class.java)

    private val handler = KitsuTrending(service)

    @Test
    fun `attempt trendingAnime`() = runBlocking {
        val job = launch {
            when (val result = handler.trendingAnime()) {
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
    fun `attempt trendingManga`() = runBlocking {
        val job = launch {
            when (val result = handler.trendingManga()) {
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
