package com.chesire.malime.kitsu.live

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.chesire.malime.core.Resource
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.kitsu.AuthProvider
import com.chesire.malime.kitsu.adapters.ImageModelAdapter
import com.chesire.malime.kitsu.adapters.SeriesStatusAdapter
import com.chesire.malime.kitsu.adapters.SeriesTypeAdapter
import com.chesire.malime.kitsu.adapters.SubtypeAdapter
import com.chesire.malime.kitsu.adapters.UserSeriesStatusAdapter
import com.chesire.malime.kitsu.api.auth.KitsuAuthService
import com.chesire.malime.kitsu.api.library.KitsuLibrary
import com.chesire.malime.kitsu.api.library.KitsuLibraryService
import com.chesire.malime.kitsu.api.library.LibrarySeriesModelAdapter
import com.chesire.malime.kitsu.api.library.ParsedRetrieveResponseAdapter
import com.chesire.malime.kitsu.interceptors.AuthInjectionInterceptor
import com.chesire.malime.kitsu.interceptors.AuthRefreshInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import org.junit.Ignore
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Ignore("Don't run these tests as part of any suite, they are for testing the apis")
class InterceptorTests {
    private val authProvider = AuthProvider(
        InstrumentationRegistry.getInstrumentation().context.getSharedPreferences(
            "Test",
            Context.MODE_PRIVATE
        )
    )

    private val authService = Retrofit.Builder()
        .baseUrl("https://kitsu.io/")
        .client(OkHttpClient())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().build()))
        .build()
        .create(KitsuAuthService::class.java)

    private val libMoshi = Moshi.Builder()
        .add(ImageModelAdapter())
        .add(SeriesStatusAdapter())
        .add(SeriesTypeAdapter())
        .add(SubtypeAdapter())
        .add(UserSeriesStatusAdapter())
        .add(ParsedRetrieveResponseAdapter())
        .add(LibrarySeriesModelAdapter())
        .build()

    private val libHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(AuthInjectionInterceptor(authProvider))
        .addInterceptor(
            AuthRefreshInterceptor(
                authProvider,
                authService
            )
        )
        .build()

    private val libService = Retrofit.Builder()
        .baseUrl("https://kitsu.io/")
        .client(libHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(MoshiConverterFactory.create(libMoshi))
        .build()
        .create(KitsuLibraryService::class.java)

    private val handler = KitsuLibrary(libService, 294558)

    @Test
    fun doTheThing() = runBlocking {
        authProvider.accessToken = "access"
        authProvider.refreshToken = "refresh"
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
}
