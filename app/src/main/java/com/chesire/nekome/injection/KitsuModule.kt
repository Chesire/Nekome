package com.chesire.nekome.injection

import com.chesire.nekome.kitsu.api.library.KitsuLibrary
import com.chesire.nekome.server.api.LibraryApi
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
     * Binds the instance of [KitsuLibrary] to [LibraryApi].
     */
    @Binds
    abstract fun providesLibraryApi(kitsuLibrary: KitsuLibrary): LibraryApi
}
