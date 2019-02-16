package com.chesire.malime.kitsu.live

import com.chesire.malime.kitsu.api.auth.KitsuAuthService
import com.chesire.malime.kitsu.api.auth.LoginRequest
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
    @Test
    fun `attempt login`() = runBlocking {
        val moshi = Moshi.Builder()
            .build()
        val service = Retrofit.Builder()
            .baseUrl("https://kitsu.io/")
            .client(OkHttpClient())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(KitsuAuthService::class.java)

        val job = launch {
            val result = service.loginAsync(
                LoginRequest(
                    "Test",
                    "Test"
                )
            ).await()

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
