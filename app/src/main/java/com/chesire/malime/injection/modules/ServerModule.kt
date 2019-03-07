package com.chesire.malime.injection.modules

import com.chesire.malime.kitsu.BuildConfig
import com.chesire.malime.kitsu.KITSU_URL
import com.chesire.malime.kitsu.adapters.ImageModelAdapter
import com.chesire.malime.kitsu.adapters.RatingSystemAdapter
import com.chesire.malime.kitsu.adapters.SeriesStatusAdapter
import com.chesire.malime.kitsu.adapters.SeriesTypeAdapter
import com.chesire.malime.kitsu.adapters.SubtypeAdapter
import com.chesire.malime.kitsu.adapters.UserSeriesStatusAdapter
import com.chesire.malime.kitsu.api.auth.KitsuAuthService
import com.chesire.malime.kitsu.api.library.KitsuLibraryService
import com.chesire.malime.kitsu.api.library.LibrarySeriesModelAdapter
import com.chesire.malime.kitsu.api.library.ParsedRetrieveResponseAdapter
import com.chesire.malime.kitsu.api.search.KitsuSearchService
import com.chesire.malime.kitsu.api.search.SearchSeriesModelAdapter
import com.chesire.malime.kitsu.api.user.KitsuUserService
import com.chesire.malime.kitsu.api.user.UserModelAdapter
import com.chesire.malime.kitsu.interceptors.AuthInjectionInterceptor
import com.chesire.malime.kitsu.interceptors.AuthRefreshInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Suppress("unused")
@Module
object ServerModule {
    @Provides
    @JvmStatic
    fun providesCoroutineCallAdapterFactory() = CoroutineCallAdapterFactory()

    @Provides
    @Reusable
    @JvmStatic
    fun providesAuthenticatedOkHttpClient(
        authInjection: AuthInjectionInterceptor,
        authRefresh: AuthRefreshInterceptor
    ): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(authInjection)
            .addInterceptor(authRefresh)
            .also { httpClient ->
                if (BuildConfig.DEBUG) {
                    httpClient.addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }
            .build()
    }

    @Provides
    @Reusable
    @JvmStatic
    fun providesAuthService(callAdapterFactory: CoroutineCallAdapterFactory): KitsuAuthService {
        return Retrofit.Builder()
            .baseUrl(KITSU_URL)
            .client(OkHttpClient())
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(
                MoshiConverterFactory.create(Moshi.Builder().build())
            )
            .build()
            .create(KitsuAuthService::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    fun providesLibraryService(
        httpClient: OkHttpClient,
        callAdapterFactory: CoroutineCallAdapterFactory
    ): KitsuLibraryService {
        val moshi = Moshi.Builder()
            .add(ImageModelAdapter())
            .add(SeriesStatusAdapter())
            .add(SeriesTypeAdapter())
            .add(SubtypeAdapter())
            .add(UserSeriesStatusAdapter())
            .add(ParsedRetrieveResponseAdapter())
            .add(LibrarySeriesModelAdapter())
            .build()

        return Retrofit.Builder()
            .baseUrl(KITSU_URL)
            .client(httpClient)
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(KitsuLibraryService::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    fun providesSearchService(
        httpClient: OkHttpClient,
        callAdapterFactory: CoroutineCallAdapterFactory
    ): KitsuSearchService {
        val moshi = Moshi.Builder()
            .add(ImageModelAdapter())
            .add(SeriesStatusAdapter())
            .add(SeriesTypeAdapter())
            .add(SearchSeriesModelAdapter())
            .add(SubtypeAdapter())
            .build()

        return Retrofit.Builder()
            .baseUrl(KITSU_URL)
            .client(httpClient)
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(KitsuSearchService::class.java)
    }

    @Provides
    @Reusable
    @JvmStatic
    fun providesUserService(
        httpClient: OkHttpClient,
        callAdapterFactory: CoroutineCallAdapterFactory
    ): KitsuUserService {
        val moshi = Moshi.Builder()
            .add(RatingSystemAdapter())
            .add(ImageModelAdapter())
            .add(UserModelAdapter())
            .build()

        return Retrofit.Builder()
            .baseUrl(KITSU_URL)
            .client(httpClient)
            .addCallAdapterFactory(callAdapterFactory)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(KitsuUserService::class.java)
    }
}
