package com.chesire.nekome.injection

import com.chesire.nekome.kitsu.KITSU_URL
import com.chesire.nekome.kitsu.adapters.ImageModelAdapter
import com.chesire.nekome.kitsu.adapters.RatingSystemAdapter
import com.chesire.nekome.kitsu.adapters.SeriesStatusAdapter
import com.chesire.nekome.kitsu.adapters.SeriesTypeAdapter
import com.chesire.nekome.kitsu.adapters.SubtypeAdapter
import com.chesire.nekome.kitsu.user.KitsuUser
import com.chesire.nekome.kitsu.user.KitsuUserEntity
import com.chesire.nekome.kitsu.user.KitsuUserEntityMapper
import com.chesire.nekome.kitsu.user.KitsuUserService
import com.chesire.nekome.user.api.UserApi
import com.chesire.nekome.user.api.UserEntityMapper
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
abstract class UserModule {

    companion object {
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

    @Binds
    abstract fun bindEntityMapper(mapper: KitsuUserEntityMapper): UserEntityMapper<KitsuUserEntity>

    @Binds
    abstract fun bindUserApi(trending: KitsuUser): UserApi
}
