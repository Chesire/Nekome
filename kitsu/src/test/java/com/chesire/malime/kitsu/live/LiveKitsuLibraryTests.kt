package com.chesire.malime.kitsu.live

import com.chesire.malime.kitsu.adapters.ImageModelAdapter
import com.chesire.malime.kitsu.adapters.SeriesStatusAdapter
import com.chesire.malime.kitsu.adapters.SeriesTypeAdapter
import com.chesire.malime.kitsu.adapters.SubtypeAdapter
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
            val callResult = service.retrieveAnime(294558, 0).await()
            if (callResult.isSuccessful) {
                val body = callResult.body()
                val s = ""
            } else {
                val errorBody = callResult.errorBody()
                val s = ""
            }
        }
    }
}
