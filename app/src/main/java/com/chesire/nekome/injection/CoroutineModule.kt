package com.chesire.nekome.injection

import com.chesire.nekome.core.IOContext
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers

/**
 * Dagger [Module] for providing [CoroutineContext] to parts of the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {
    /**
     * Provides a [CoroutineContext] of [Dispatchers.IO] providing the annotation [IOContext] is used.
     */
    @Provides
    @IOContext
    fun providesIOContext(): CoroutineContext = Dispatchers.IO
}
