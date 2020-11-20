package com.chesire.nekome.injection

import com.chesire.nekome.kitsu.BuildConfig
import com.chesire.nekome.kitsu.KITSU_URL
import com.chesire.nekome.kitsu.adapters.ImageModelAdapter
import com.chesire.nekome.kitsu.adapters.SeriesStatusAdapter
import com.chesire.nekome.kitsu.adapters.SeriesTypeAdapter
import com.chesire.nekome.kitsu.adapters.SubtypeAdapter
import com.chesire.nekome.kitsu.adapters.UserSeriesStatusAdapter
import com.chesire.nekome.kitsu.api.library.KitsuLibraryService
import com.chesire.nekome.kitsu.api.library.LibrarySeriesModelAdapter
import com.chesire.nekome.kitsu.api.library.ParsedRetrieveResponseAdapter
import com.chesire.nekome.kitsu.interceptors.AuthInjectionInterceptor
import com.chesire.nekome.kitsu.interceptors.AuthRefreshInterceptor
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Dagger [Module] for the [com.chesire.nekome.server] package.
 */
@Module
@InstallIn(ApplicationComponent::class)
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
}
