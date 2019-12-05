package com.chesire.nekome.injection.modules

import com.chesire.nekome.core.url.UrlHandler
import com.chesire.nekome.harness.FakeUrlHandler
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class FakeUrlModule {
    @Binds
    abstract fun providesFakeUrlHandler(fakeUrlHandler: FakeUrlHandler): UrlHandler
}
