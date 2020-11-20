package com.chesire.nekome.injection

import com.chesire.nekome.kitsu.KITSU_URL
import com.chesire.nekome.kitsu.adapters.ImageModelAdapter
import com.chesire.nekome.kitsu.adapters.SeriesStatusAdapter
import com.chesire.nekome.kitsu.adapters.SeriesTypeAdapter
import com.chesire.nekome.kitsu.adapters.SubtypeAdapter
import com.chesire.nekome.kitsu.search.KitsuSearch
import com.chesire.nekome.kitsu.search.KitsuSearchEntity
import com.chesire.nekome.kitsu.search.KitsuSearchEntityMapper
import com.chesire.nekome.kitsu.search.KitsuSearchService
import com.chesire.nekome.search.api.SearchApi
import com.chesire.nekome.search.api.SearchEntityMapper
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

    @Binds
    abstract fun bindEntityMapper(mapper: KitsuSearchEntityMapper): SearchEntityMapper<KitsuSearchEntity>

    @Binds
    abstract fun bindApi(api: KitsuSearch): SearchApi
}
