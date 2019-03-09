package com.chesire.malime.injection.modules

import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.kitsu.api.auth.KitsuAuth
import com.chesire.malime.kitsu.api.library.KitsuLibrary
import dagger.Binds
import dagger.Module

@Module
abstract class KitsuModule {
    @Binds
    abstract fun providesAuthApi(kitsuAuth: KitsuAuth): AuthApi

    @Binds
    abstract fun providesLibraryApi(kitsuLibrary: KitsuLibrary): LibraryApi
}
