package com.chesire.malime.injection.modules

import com.chesire.malime.IOContext
import com.chesire.malime.MainContext
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Suppress("unused")
@Module
object CoroutinesModule {
    @IOContext
    @Provides
    @JvmStatic
    fun providesIOCoroutineContext(): CoroutineContext = Dispatchers.IO

    @MainContext
    @Provides
    @JvmStatic
    fun providesMainCoroutineContext(): CoroutineContext = Dispatchers.Main
}
