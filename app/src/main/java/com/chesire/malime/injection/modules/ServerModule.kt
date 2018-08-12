package com.chesire.malime.injection.modules

import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.core.api.Authorizer
import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.core.repositories.Authorization
import com.chesire.malime.kitsu.BuildConfig
import com.chesire.malime.kitsu.KITSU_ENDPOINT
import com.chesire.malime.kitsu.api.KitsuAuthInterceptor
import com.chesire.malime.kitsu.api.KitsuAuthorizer
import com.chesire.malime.kitsu.api.KitsuManager
import com.chesire.malime.kitsu.api.KitsuService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Suppress("unused")
@Module
internal class ServerModule {
    @Singleton
    @Provides
    fun providesAuthorization(
        kitsuAuthorizer: KitsuAuthorizer
        // malAuthorizer: MalAuthorizer
    ): Authorization {
        return Authorization(
            mapOf(
                SupportedService.Kitsu to kitsuAuthorizer
                // Pair(SupportedService.MyAnimeList, malAuthorizer)
            )
        )
    }

    // for now we can just return the KitsuManager, as we don't support anything else yet
    @Provides
    fun providesAuthApi(manager: KitsuManager): AuthApi = manager

    // for now we can just return the KitsuManager, as we don't support anything else yet
    @Provides
    fun providesMalimeApi(manager: KitsuManager): LibraryApi = manager

    // for now we can just return the KitsuManager, as we don't support anything else yet
    @Provides
    fun providesSearchApi(manager: KitsuManager): SearchApi = manager

    // for now we can just return the KitsuAuthorizer, as we don't support anything else yet
    @Provides
    fun providesAuthorizer(authorizer: KitsuAuthorizer): Authorizer<*> = authorizer

    @Singleton
    @Provides
    fun providesKitsuService(kitsuAuthorizer: KitsuAuthorizer): KitsuService {
        val httpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor(KitsuAuthInterceptor(kitsuAuthorizer))

        if (BuildConfig.DEBUG) {
            val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
                this.level = HttpLoggingInterceptor.Level.BODY
            }

            httpClient.addInterceptor(interceptor)
        }

        return Retrofit.Builder()
            .baseUrl(KITSU_ENDPOINT)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KitsuService::class.java)
    }
}