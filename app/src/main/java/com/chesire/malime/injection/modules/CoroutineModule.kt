package com.chesire.malime.injection.modules

import com.chesire.malime.IOContext
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Suppress("unused")
@Module
object CoroutineModule {
    @Provides
    @IOContext
    @JvmStatic
    fun providesIOContext(): CoroutineContext = Dispatchers.IO
}
