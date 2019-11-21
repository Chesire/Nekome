package com.chesire.malime.injection.modules

import com.chesire.malime.kitsu.api.auth.KitsuAuth
import com.chesire.malime.kitsu.api.library.KitsuLibrary
import com.chesire.malime.kitsu.api.search.KitsuSearch
import com.chesire.malime.kitsu.api.trending.KitsuTrending
import com.chesire.malime.kitsu.api.user.KitsuUser
import com.chesire.malime.server.api.AuthApi
import com.chesire.malime.server.api.LibraryApi
import com.chesire.malime.server.api.SearchApi
import com.chesire.malime.server.api.TrendingApi
import com.chesire.malime.server.api.UserApi
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class KitsuModule {
    @Binds
    abstract fun providesAuthApi(kitsuAuth: KitsuAuth): AuthApi

    @Binds
    abstract fun providesLibraryApi(kitsuLibrary: KitsuLibrary): LibraryApi

    @Binds
    abstract fun providesSearchApi(kitsuSearch: KitsuSearch): SearchApi

    @Binds
    abstract fun providesTrendingApi(kitsuTrending: KitsuTrending): TrendingApi

    @Binds
    abstract fun providesUserApi(kitsuUser: KitsuUser): UserApi
}
