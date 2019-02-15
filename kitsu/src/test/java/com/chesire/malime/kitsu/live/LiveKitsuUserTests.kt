package com.chesire.malime.kitsu.live

import com.chesire.malime.kitsu.adapters.RatingSystemAdapter
import com.chesire.malime.kitsu.api.KitsuUserService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import org.junit.Ignore
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Ignore("Don't run these tests as part of any suite, they are for testing the apis")
class LiveKitsuUserTests {
    @Test
    fun `attempt getUserDetails`() = runBlocking {
        val moshi = Moshi.Builder()
            .add(RatingSystemAdapter())
            .build()

        val httpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(KitsuAuthInterceptor())
            .build()

        val service = Retrofit.Builder()
            .baseUrl("https://kitsu.io/")
            .client(httpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(KitsuUserService::class.java)

        val job = launch {
            val result = service.getUserDetailsAsync().await()

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

class KitsuAuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        // DO NOT COMMIT
        val authenticatedRequest = request.newBuilder()
            .header(
                "Authorization",
                "Bearer SECRET"
            )
            .build()

        return chain.proceed(authenticatedRequest)
    }
}
