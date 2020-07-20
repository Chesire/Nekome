package com.chesire.nekome.injection.modules

import com.chesire.nekome.kitsu.BuildConfig
import com.chesire.nekome.kitsu.KITSU_URL
import com.chesire.nekome.kitsu.adapters.ImageModelAdapter
import com.chesire.nekome.kitsu.adapters.RatingSystemAdapter
import com.chesire.nekome.kitsu.adapters.SeriesStatusAdapter
import com.chesire.nekome.kitsu.adapters.SeriesTypeAdapter
import com.chesire.nekome.kitsu.adapters.SubtypeAdapter
import com.chesire.nekome.kitsu.adapters.UserSeriesStatusAdapter
import com.chesire.nekome.kitsu.api.auth.KitsuAuthService
import com.chesire.nekome.kitsu.api.library.KitsuLibraryService
import com.chesire.nekome.kitsu.api.library.LibrarySeriesModelAdapter
import com.chesire.nekome.kitsu.api.library.ParsedRetrieveResponseAdapter
import com.chesire.nekome.kitsu.api.search.KitsuSearchService
import com.chesire.nekome.kitsu.api.search.SearchSeriesModelAdapter
import com.chesire.nekome.kitsu.api.trending.KitsuTrendingService
import com.chesire.nekome.kitsu.api.trending.TrendingAdapter
import com.chesire.nekome.kitsu.api.user.KitsuUserService
import com.chesire.nekome.kitsu.api.user.UserModelAdapter
import com.chesire.nekome.kitsu.interceptors.AuthInjectionInterceptor
import com.chesire.nekome.kitsu.interceptors.AuthRefreshInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Dagger [Module] for the [com.chesire.nekome.server] package.
 */
@Suppress("unused")
@Module
object ServerModule {
    /**
     * Provides an instance of [OkHttpClient] with the authentication injectors pre-setup, so that
     * authentication is already handled.
     */
    @Provides
    @Reusable
    fun providesAuthenticatedClient(
        authInjection: AuthInjectionInterceptor,
        authRefresh: AuthRefreshInterceptor
    ): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .addInterceptor(authInjection)
            .addInterceptor(authRefresh)
            .also { httpClient ->
                if (BuildConfig.DEBUG) {
                    httpClient.addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                }
            }
            .build()
    }

    /**
     * Builds and provides the instance of [KitsuAuthService].
     */
    @Provides
    @Reusable
    fun providesAuthService(): KitsuAuthService {
        return Retrofit.Builder()
            .baseUrl(KITSU_URL)
            .client(OkHttpClient())
            .addConverterFactory(
                MoshiConverterFactory.create(Moshi.Builder().build())
            )
            .build()
            .create(KitsuAuthService::class.java)
    }

    /**
     * Builds and provides the instance of [KitsuLibraryService].
     */
    @Provides
    @Reusable
    fun providesLibraryService(httpClient: OkHttpClient): KitsuLibraryService {
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
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(KitsuLibraryService::class.java)
    }

    /**
     * Builds and provides the instance of [KitsuSearchService].
     */
    @Provides
    @Reusable
    fun providesSearchService(httpClient: OkHttpClient): KitsuSearchService {
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
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(KitsuSearchService::class.java)
    }

    /**
     * Builds and provides the instance of [KitsuTrendingService].
     */
    @Provides
    @Reusable
    fun providesTrendingService(httpClient: OkHttpClient): KitsuTrendingService {
        val moshi = Moshi.Builder()
            .add(ImageModelAdapter())
            .add(SeriesStatusAdapter())
            .add(SeriesTypeAdapter())
            .add(SubtypeAdapter())
            .add(TrendingAdapter())
            .build()

        return Retrofit.Builder()
            .baseUrl(KITSU_URL)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(KitsuTrendingService::class.java)
    }

    /**
     * Builds and provides the instance of [KitsuUserService].
     */
    @Provides
    @Reusable
    fun providesUserService(httpClient: OkHttpClient): KitsuUserService {
        val moshi = Moshi.Builder()
            .add(RatingSystemAdapter())
            .add(ImageModelAdapter())
            .add(UserModelAdapter())
            .build()

        return Retrofit.Builder()
            .baseUrl(KITSU_URL)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(KitsuUserService::class.java)
    }
}
