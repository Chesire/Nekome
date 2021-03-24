package com.chesire.nekome.injection

import com.chesire.nekome.kitsu.BuildConfig
import com.chesire.nekome.datasource.auth.remote.AuthInjectionInterceptor
import com.chesire.nekome.datasource.auth.remote.AuthRefreshInterceptor
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Dagger [Module] for the [com.chesire.nekome.server] package.
 */
@Module
@InstallIn(SingletonComponent::class)
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
}
