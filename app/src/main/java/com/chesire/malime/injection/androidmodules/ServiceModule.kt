package com.chesire.malime.injection.androidmodules

import com.chesire.malime.service.PeriodicUpdateService
import com.chesire.malime.service.RefreshTokenService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ServiceModule {
    @ContributesAndroidInjector
    internal abstract fun contributePeriodicUpdateService(): PeriodicUpdateService

    @ContributesAndroidInjector
    internal abstract fun contributeRefreshTokenService(): RefreshTokenService
}
