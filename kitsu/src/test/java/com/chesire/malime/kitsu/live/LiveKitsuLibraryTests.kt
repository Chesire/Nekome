package com.chesire.malime.kitsu.live

import com.chesire.malime.core.Resource
import com.chesire.malime.core.flags.SeriesType
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.kitsu.adapters.ImageModelAdapter
import com.chesire.malime.kitsu.adapters.SeriesStatusAdapter
import com.chesire.malime.kitsu.adapters.SeriesTypeAdapter
import com.chesire.malime.kitsu.adapters.SubtypeAdapter
import com.chesire.malime.kitsu.adapters.UserSeriesStatusAdapter
import com.chesire.malime.kitsu.api.library.KitsuLibrary
import com.chesire.malime.kitsu.api.library.KitsuLibraryService
import com.chesire.malime.kitsu.api.library.ParsedRetrieveResponseAdapter
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.RequestBody
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
        val json = createNewAddModel(294558, 550, SeriesType.Anime)
        val body = RequestBody.create(MediaType.parse("application/vnd.api+json"), json)
        val result = service.addAnime(body).await()

        if (result.isSuccessful) {
            val body = result.body()
            val s = ""
        } else {
            val error = result.errorBody()?.string()
            val s = ""
        }
    }
}

private fun createNewAddModel(userId: Int, seriesId: Int, seriesType: SeriesType): String {
    return """
{
  "data": {
    "type": "libraryEntries",
    "attributes": {
      "progress": 0,
      "status": ${UserSeriesStatus.Current}
    },
    "relationships": {
      "$seriesType": {
        "data": {
          "type": $seriesType,
          "id": $seriesId
        }
      },
      "user": {
        "data": {
          "type": "users",
          "id": $userId
        }
      }
    }
  }
}""".trimIndent()
}
