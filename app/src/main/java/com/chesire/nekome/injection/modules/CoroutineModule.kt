package com.chesire.nekome.injection.modules

import com.chesire.nekome.core.IOContext
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Dagger [Module] for providing [CoroutineContext] to parts of the application.
 */
@Module
@InstallIn(ApplicationComponent::class)
object CoroutineModule {
    /**
     * Provides a [CoroutineContext] of [Dispatchers.IO] providing the annotation [IOContext] is used.
     */
    @Provides
    @IOContext
    fun providesIOContext(): CoroutineContext = Dispatchers.IO
}
