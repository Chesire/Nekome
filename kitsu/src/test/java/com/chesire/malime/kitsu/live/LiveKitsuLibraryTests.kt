package com.chesire.malime.kitsu.live

import com.chesire.malime.kitsu.adapters.ImageModelAdapter
import com.chesire.malime.kitsu.adapters.SeriesStatusAdapter
import com.chesire.malime.kitsu.adapters.SeriesTypeAdapter
import com.chesire.malime.kitsu.adapters.SubtypeAdapter
import com.chesire.malime.kitsu.adapters.UserSeriesStatusAdapter
import com.chesire.malime.kitsu.api.library.KitsuLibrary
import com.chesire.malime.kitsu.api.library.KitsuLibraryService
import com.chesire.malime.kitsu.api.library.ParsedLibraryResponseAdapter
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
        .add(ParsedLibraryResponseAdapter())
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
    fun `attempt retrieveLibrary`() = runBlocking {
        val job = launch {
            val animeCall = service.retrieveAnimeAsync(294558, 0)
            val mangaCall = service.retrieveMangaAsync(294558, 0)

            val animeResult = animeCall.await()
            val mangaResult = mangaCall.await()

            if (animeResult.isSuccessful) {
                val body = animeResult.body()
                val s = ""
            } else {
                val errorBody = animeResult.errorBody()
                val s = ""
            }
            if (mangaResult.isSuccessful) {
                val body = mangaResult.body()
                val s = ""
            } else {
                val errorBody = mangaResult.errorBody()
                val s = ""
            }

            // Need to check pulling from the offset
        }
    }
}
