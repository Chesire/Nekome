package com.chesire.nekome.injection

import com.chesire.nekome.core.url.CustomTabsUrl
import com.chesire.nekome.core.url.UrlHandler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Dagger [Module] to bind [UrlHandler].
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class UrlModule {
    /**
     * Binds the instance of [CustomTabsUrl] to [UrlHandler].
     */
    @Binds
    abstract fun providesUrlHandler(urlHandler: CustomTabsUrl): UrlHandler
}
