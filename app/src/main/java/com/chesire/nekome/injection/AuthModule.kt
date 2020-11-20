package com.chesire.nekome.injection

import com.chesire.nekome.auth.api.AuthApi
import com.chesire.nekome.kitsu.KITSU_URL
import com.chesire.nekome.kitsu.auth.KitsuAuth
import com.chesire.nekome.kitsu.auth.KitsuAuthService
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(ApplicationComponent::class)
abstract class AuthModule {

    companion object {
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
    }

    @Binds
    abstract fun bindApi(api: KitsuAuth): AuthApi
}
