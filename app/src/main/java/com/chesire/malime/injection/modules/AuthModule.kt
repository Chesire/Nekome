package com.chesire.malime.injection.modules

import com.chesire.malime.AuthCaster
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Suppress("unused")
@Module
object AuthModule {
    @Provides
    @Singleton
    @JvmStatic
    fun provideAuthCaster(): AuthCaster = AuthCaster
}
