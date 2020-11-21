package com.chesire.nekome.injection

import com.chesire.nekome.core.EntityMapper
import com.chesire.nekome.kitsu.KITSU_URL
import com.chesire.nekome.kitsu.adapters.ImageModelAdapter
import com.chesire.nekome.kitsu.adapters.SeriesStatusAdapter
import com.chesire.nekome.kitsu.adapters.SeriesTypeAdapter
import com.chesire.nekome.kitsu.adapters.SubtypeAdapter
import com.chesire.nekome.kitsu.trending.KitsuTrending
import com.chesire.nekome.kitsu.trending.KitsuTrendingEntity
import com.chesire.nekome.kitsu.trending.KitsuTrendingEntityMapper
import com.chesire.nekome.kitsu.trending.KitsuTrendingService
import com.chesire.nekome.trending.api.TrendingApi
import com.chesire.nekome.trending.api.TrendingEntity
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

    @Binds
    abstract fun bindEntityMapper(
        mapper: KitsuTrendingEntityMapper
    ): EntityMapper<KitsuTrendingEntity, TrendingEntity>

    @Binds
    abstract fun bindApi(api: KitsuTrending): TrendingApi
}
