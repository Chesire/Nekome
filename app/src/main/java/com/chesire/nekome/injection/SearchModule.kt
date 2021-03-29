package com.chesire.nekome.injection

import com.chesire.nekome.datasource.search.remote.SearchApi
import com.chesire.nekome.kitsu.KITSU_URL
import com.chesire.nekome.kitsu.adapters.ImageModelAdapter
import com.chesire.nekome.kitsu.adapters.SeriesStatusAdapter
import com.chesire.nekome.kitsu.adapters.SeriesTypeAdapter
import com.chesire.nekome.kitsu.adapters.SubtypeAdapter
import com.chesire.nekome.kitsu.search.KitsuSearch
import com.chesire.nekome.kitsu.search.KitsuSearchService
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
 * Provides a Hilt module for usage of [SearchApi].
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class SearchModule {

    companion object {
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
                .add(SubtypeAdapter())
                .build()

            return Retrofit.Builder()
                .baseUrl(KITSU_URL)
                .client(httpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(KitsuSearchService::class.java)
        }
    }

    /**
     * Binds [api] to an instance of [SearchApi].
     */
    @Binds
    abstract fun bindApi(api: KitsuSearch): SearchApi
}
