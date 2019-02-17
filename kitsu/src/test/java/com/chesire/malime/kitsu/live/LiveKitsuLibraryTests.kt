package com.chesire.malime.kitsu.live

import com.chesire.malime.core.Resource
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.kitsu.adapters.ImageModelAdapter
import com.chesire.malime.kitsu.adapters.SeriesStatusAdapter
import com.chesire.malime.kitsu.adapters.SeriesTypeAdapter
import com.chesire.malime.kitsu.adapters.SubtypeAdapter
import com.chesire.malime.kitsu.adapters.UserSeriesStatusAdapter
import com.chesire.malime.kitsu.api.library.KitsuLibrary
import com.chesire.malime.kitsu.api.library.KitsuLibraryService
import com.chesire.malime.kitsu.api.library.LibrarySeriesModelAdapter
import com.chesire.malime.kitsu.api.library.ParsedRetrieveResponseAdapter
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
class LiveKitsuLibraryTests {
    private val moshi = Moshi.Builder()
        .add(ImageModelAdapter())
        .add(SeriesStatusAdapter())
        .add(SeriesTypeAdapter())
        .add(SubtypeAdapter())
        .add(UserSeriesStatusAdapter())
        .add(ParsedRetrieveResponseAdapter())
        .add(LibrarySeriesModelAdapter())
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
        .create(KitsuLibraryService::class.java)

    private val handler = KitsuLibrary(service, 294558)

    @Test
    fun `attempt retrieveAnime`() = runBlocking {
        val job = launch {
            val result = handler.retrieveAnime()

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
    fun `attempt retrieveManga`() = runBlocking {
        val job = launch {
            val result = handler.retrieveManga()

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
    fun `attempt addAnime`() = runBlocking {
        val result = handler.addAnime(556, UserSeriesStatus.Current)

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
