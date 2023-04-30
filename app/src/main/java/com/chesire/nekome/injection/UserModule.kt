package com.chesire.nekome.injection

import com.chesire.nekome.datasource.user.remote.UserApi
import com.chesire.nekome.kitsu.KITSU_URL
import com.chesire.nekome.kitsu.adapters.ImageModelAdapter
import com.chesire.nekome.kitsu.user.KitsuUser
import com.chesire.nekome.kitsu.user.KitsuUserService
import com.chesire.nekome.kitsu.user.adapter.RatingSystemAdapter
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Provides a Hilt module for usage of [UserApi].
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {

    companion object {

        /**
         * Builds and provides the instance of [KitsuUserService].
         */
        @Provides
        @Reusable
        fun providesUserService(httpClient: OkHttpClient): KitsuUserService {
            val moshi = Moshi.Builder()
                .add(RatingSystemAdapter())
                .add(ImageModelAdapter())
                .build()

            return Retrofit.Builder()
                .baseUrl(KITSU_URL)
                .client(httpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(KitsuUserService::class.java)
        }
    }

    /**
     * Binds [api] to an instance of [UserApi].
     */
    @Binds
    abstract fun bindApi(api: KitsuUser): UserApi
}
