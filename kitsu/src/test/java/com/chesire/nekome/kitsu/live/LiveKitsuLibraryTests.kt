package com.chesire.nekome.kitsu.live

import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.kitsu.adapters.ImageModelAdapter
import com.chesire.nekome.kitsu.adapters.SeriesStatusAdapter
import com.chesire.nekome.kitsu.adapters.SeriesTypeAdapter
import com.chesire.nekome.kitsu.adapters.SubtypeAdapter
import com.chesire.nekome.kitsu.adapters.UserSeriesStatusAdapter
import com.chesire.nekome.kitsu.api.library.KitsuLibrary
import com.chesire.nekome.kitsu.api.library.KitsuLibraryService
import com.chesire.nekome.kitsu.api.library.LibrarySeriesModelAdapter
import com.chesire.nekome.kitsu.api.library.ParsedRetrieveResponseAdapter
import com.chesire.nekome.server.Resource
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
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(KitsuLibraryService::class.java)

    private val handler = KitsuLibrary(service)

    @Test
    fun `attempt retrieveAnime`() = runBlocking {
        val job = launch {
            val result = handler.retrieveAnime(294558)

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
            val result = handler.retrieveManga(294558)

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
        val result = handler.addAnime(294558, 556, UserSeriesStatus.Current)

        when (result) {
            is Resource.Success -> {
                val data = result.data
            }
            is Resource.Error -> {
                val error = result.msg
            }
        }
    }

    @Test
    fun `attempt addManga`() = runBlocking {
        val result = handler.addManga(294558, 26999, UserSeriesStatus.Current)

        when (result) {
            is Resource.Success -> {
                val data = result.data
            }
            is Resource.Error -> {
                val error = result.msg
            }
        }
    }

    @Test
    fun `attempt updateSeries`() = runBlocking {
        val result = handler.update(30261187, 1, UserSeriesStatus.Current)

        when (result) {
            is Resource.Success -> {
                val data = result.data
            }
            is Resource.Error -> {
                val error = result.msg
            }
        }
    }

    @Test
    fun `attempt delete`() = runBlocking {
        val result = handler.delete(29461572)

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
