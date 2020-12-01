package com.chesire.nekome.injection

import com.chesire.nekome.kitsu.KITSU_URL
import com.chesire.nekome.kitsu.adapters.ImageModelAdapter
import com.chesire.nekome.kitsu.adapters.SeriesStatusAdapter
import com.chesire.nekome.kitsu.adapters.SeriesTypeAdapter
import com.chesire.nekome.kitsu.adapters.SubtypeAdapter
import com.chesire.nekome.kitsu.trending.KitsuTrending
import com.chesire.nekome.kitsu.trending.KitsuTrendingService
import com.chesire.nekome.trending.api.TrendingApi
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
 * Provides a Hilt module for usage of [TrendingApi].
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class TrendingModule {

    companion object {
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
                .build()

            return Retrofit.Builder()
                .baseUrl(KITSU_URL)
                .client(httpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(KitsuTrendingService::class.java)
        }
    }

    /**
     * Binds [api] to an instance of [TrendingApi].
     */
    @Binds
    abstract fun bindApi(api: KitsuTrending): TrendingApi
}
