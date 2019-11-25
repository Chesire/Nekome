package com.chesire.nekome.injection.modules

import com.chesire.nekome.core.IOContext
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Dagger [Module] for providing [CoroutineContext] to parts of the application.
 */
@Suppress("unused")
@Module
object CoroutineModule {
    /**
     * Provides a [CoroutineContext] of [Dispatchers.IO] providing the annotation [IOContext] is used.
     */
    @Provides
    @IOContext
    fun providesIOContext(): CoroutineContext = Dispatchers.IO
}
