package com.chesire.nekome.injection

import com.chesire.nekome.kitsu.api.auth.KitsuAuth
import com.chesire.nekome.kitsu.api.library.KitsuLibrary
import com.chesire.nekome.kitsu.api.search.KitsuSearch
import com.chesire.nekome.kitsu.api.user.KitsuUser
import com.chesire.nekome.server.api.AuthApi
import com.chesire.nekome.server.api.LibraryApi
import com.chesire.nekome.server.api.SearchApi
import com.chesire.nekome.server.api.UserApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

/**
 * Dagger [Module] for the [com.chesire.nekome.kitsu] package.
 */
@Module
@InstallIn(ApplicationComponent::class)
abstract class KitsuModule {
    /**
     * Binds the instance of [KitsuAuth] to [AuthApi].
     */
    @Binds
    abstract fun providesAuthApi(kitsuAuth: KitsuAuth): AuthApi

    /**
     * Binds the instance of [KitsuLibrary] to [LibraryApi].
     */
    @Binds
    abstract fun providesLibraryApi(kitsuLibrary: KitsuLibrary): LibraryApi

    /**
     * Binds the instance of [KitsuSearch] to [SearchApi].
     */
    @Binds
    abstract fun providesSearchApi(kitsuSearch: KitsuSearch): SearchApi

    /**
     * Binds the instance of [KitsuUser] to [UserApi].
     */
    @Binds
    abstract fun providesUserApi(kitsuUser: KitsuUser): UserApi
}
