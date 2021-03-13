package com.chesire.nekome.injection

import com.chesire.nekome.activity.api.ActivityApi
import com.chesire.nekome.kitsu.KITSU_URL
import com.chesire.nekome.kitsu.activity.KitsuActivity
import com.chesire.nekome.kitsu.activity.KitsuActivityService
import com.chesire.nekome.kitsu.activity.adapter.ChangedDataContainerAdapter
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
 * Provides a Hilt module for usage of [ActivityApi].
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ActivityModule {

    companion object {

        /**
         * Builds and provides the instance of [KitsuAutKitsuActivityServicehService].
         */
        @Provides
        @Reusable
        fun providesActivityService(httpClient: OkHttpClient): KitsuActivityService {
            val moshi = Moshi.Builder()
                .add(ChangedDataContainerAdapter())
                .build()

            return Retrofit.Builder()
                .baseUrl(KITSU_URL)
                .client(httpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(KitsuActivityService::class.java)
        }
    }

    /**
     * Binds [api] to an instance of [ActivityApi].
     */
    @Binds
    abstract fun bindApi(api: KitsuActivity): ActivityApi
}
