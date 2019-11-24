package com.chesire.nekome.injection.modules

import com.chesire.nekome.core.IOContext
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Suppress("unused")
@Module
object CoroutineModule {
    @Provides
    @IOContext
    fun providesIOContext(): CoroutineContext = Dispatchers.IO
}
