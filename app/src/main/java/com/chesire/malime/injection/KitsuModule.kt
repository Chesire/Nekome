package com.chesire.malime.injection

import com.chesire.malime.kitsu.api.KitsuManagerFactory
import dagger.Module
import dagger.Provides

@Module
class KitsuModule {
    @Provides
    fun providesKitsuManagerFactory(): KitsuManagerFactory {
        return KitsuManagerFactory()
    }
}