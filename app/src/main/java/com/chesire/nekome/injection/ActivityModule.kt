package com.chesire.nekome.injection

import com.chesire.nekome.kitsu.KITSU_URL
import com.chesire.nekome.kitsu.activity.KitsuActivityService
import com.chesire.nekome.kitsu.activity.adapter.EventAdapter
import com.chesire.nekome.kitsu.activity.adapter.KindAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
abstract class ActivityModule {

    companion object {

        @Provides
        @Reusable
        fun providesActivityService(httpClient: OkHttpClient): KitsuActivityService {
            val moshi = Moshi.Builder()
                .add(EventAdapter())
                .add(KindAdapter())
                .build()

            return Retrofit.Builder()
                .baseUrl(KITSU_URL)
                .client(httpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(KitsuActivityService::class.java)
        }
    }

    //@Binds
    //abstract fun bindApi(api: KitsuActivity): ActivityApi
}
