package com.chesire.nekome.injection.modules

import com.chesire.nekome.core.url.CustomTabsUrl
import com.chesire.nekome.core.url.UrlHandler
import dagger.Binds
import dagger.Module

/**
 * Dagger [Module] to bind [UrlHandler].
 */
@Suppress("unused")
@Module
abstract class UrlModule {
    /**
     * Binds the instance of [CustomTabsUrl] to [UrlHandler].
     */
    @Binds
    abstract fun providesUrlHandler(urlHandler: CustomTabsUrl): UrlHandler
}
