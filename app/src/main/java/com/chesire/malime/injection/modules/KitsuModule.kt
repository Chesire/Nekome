package com.chesire.malime.injection.modules

import com.chesire.malime.kitsu.api.KitsuManagerFactory
import dagger.Module
import dagger.Provides

@Module
internal class KitsuModule {
    @Provides
    fun providesKitsuManagerFactory(): KitsuManagerFactory {
        return KitsuManagerFactory()
    }
}