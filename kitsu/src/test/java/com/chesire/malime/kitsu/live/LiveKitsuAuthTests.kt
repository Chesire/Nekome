package com.chesire.malime.kitsu.live

import com.chesire.malime.core.Resource
import com.chesire.malime.kitsu.api.auth.KitsuAuth
import com.chesire.malime.kitsu.api.auth.KitsuAuthService
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
class LiveKitsuAuthTests {
    private val moshi = Moshi.Builder()
        .build()

    private val service = Retrofit.Builder()
        .baseUrl("https://kitsu.io/")
        .client(OkHttpClient())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(KitsuAuthService::class.java)

    private val handler = KitsuAuth(service)

    @Test
    fun `attempt login`() = runBlocking {
        val job = launch {
            val result = handler.login("Test", "Test")

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
