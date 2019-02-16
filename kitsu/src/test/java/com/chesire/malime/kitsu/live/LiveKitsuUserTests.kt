package com.chesire.malime.kitsu.live

import com.chesire.malime.core.Resource
import com.chesire.malime.kitsu.adapters.ImageModelAdapter
import com.chesire.malime.kitsu.adapters.RatingSystemAdapter
import com.chesire.malime.kitsu.adapters.UserModelAdapter
import com.chesire.malime.kitsu.api.user.KitsuUser
import com.chesire.malime.kitsu.api.user.KitsuUserService
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
class LiveKitsuUserTests {
    private val moshi = Moshi.Builder()
        .add(RatingSystemAdapter())
        .add(ImageModelAdapter())
        .add(UserModelAdapter())
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
        .create(KitsuUserService::class.java)

    private val handler = KitsuUser(service)

    @Test
    fun `attempt getUserDetails`() = runBlocking {
        val job = launch {
            val result = handler.getUserDetails()

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

