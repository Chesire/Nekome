package com.chesire.malime.kitsu.live

import com.chesire.malime.kitsu.adapters.ImageModelAdapter
import com.chesire.malime.kitsu.adapters.SeriesModelAdapter
import com.chesire.malime.kitsu.adapters.SeriesStatusAdapter
import com.chesire.malime.kitsu.adapters.SeriesTypeAdapter
import com.chesire.malime.kitsu.adapters.SubtypeAdapter
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
    @Test
    fun `attempt searchForAnime`() = runBlocking {
        val moshi = Moshi.Builder()
            .add(ImageModelAdapter())
            .add(SeriesStatusAdapter())
            .add(SeriesTypeAdapter())
            .add(SeriesModelAdapter())
            .add(SubtypeAdapter())
            .build()

        val httpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(LiveTestAuthInterceptor())
            .build()

        val service = Retrofit.Builder()
            .baseUrl("https://kitsu.io/")
            .client(httpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(KitsuSearchService::class.java)

        val job = launch {
            val result = service.searchForAnimeAsync("Radiant").await()

            if (result.isSuccessful) {
                val body = result.body()
                val s = "Test"
            } else {
                val error = result.errorBody()?.string()
                val s = "Test"
            }
        }
    }
}
