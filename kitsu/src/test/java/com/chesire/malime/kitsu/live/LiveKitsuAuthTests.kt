package com.chesire.malime.kitsu.live

import com.chesire.malime.kitsu.api.KitsuAuthService
import com.chesire.malime.kitsu.models.request.LoginRequest
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Ignore("Don't run these tests as part of any suite, they are for testing the apis")
class LiveKitsuAuthTests {
    @Test
    fun `attempt login`() {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val service = Retrofit.Builder()
            .baseUrl("https://kitsu.io/")
            .client(OkHttpClient())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(KitsuAuthService::class.java)

        val call = service.login(LoginRequest("Test", "Test"))
        val result = call.execute()
        if (result.isSuccessful) {
            val body = result.body()
            val s = "Test"
        } else {
            val error = result.errorBody()?.string()
            val s = "Test"
        }
    }
}
