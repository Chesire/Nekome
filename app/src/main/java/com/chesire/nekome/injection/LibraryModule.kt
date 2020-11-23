package com.chesire.nekome.injection

import com.chesire.nekome.core.EntityMapper
import com.chesire.nekome.kitsu.KITSU_URL
import com.chesire.nekome.kitsu.adapters.ImageModelAdapter
import com.chesire.nekome.kitsu.adapters.SeriesStatusAdapter
import com.chesire.nekome.kitsu.adapters.SeriesTypeAdapter
import com.chesire.nekome.kitsu.adapters.SubtypeAdapter
import com.chesire.nekome.kitsu.library.KitsuLibrary
import com.chesire.nekome.kitsu.library.KitsuLibraryEntityMapper
import com.chesire.nekome.kitsu.library.KitsuLibraryService
import com.chesire.nekome.kitsu.library.adapter.UserSeriesStatusAdapter
import com.chesire.nekome.kitsu.library.entity.KitsuLibraryEntity
import com.chesire.nekome.kitsu.trending.KitsuTrendingService
import com.chesire.nekome.library.api.LibraryApi
import com.chesire.nekome.library.api.LibraryEntity
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
abstract class LibraryModule {

    companion object {
        /**
         * Builds and provides the instance of [KitsuTrendingService].
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
                .build()

            return Retrofit.Builder()
                .baseUrl(KITSU_URL)
                .client(httpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(KitsuLibraryService::class.java)
        }
    }

    @Binds
    abstract fun bindEntityMapper(
        mapper: KitsuLibraryEntityMapper
    ): EntityMapper<KitsuLibraryEntity, LibraryEntity?>

    @Binds
    abstract fun bindApi(api: KitsuLibrary): LibraryApi
}
