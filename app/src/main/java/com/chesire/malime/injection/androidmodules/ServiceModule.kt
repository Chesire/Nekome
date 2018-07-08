package com.chesire.malime.injection.androidmodules

import com.chesire.malime.service.PeriodicUpdateService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ServiceModule {
    @ContributesAndroidInjector
    internal abstract fun contributePeriodicUpdateService(): PeriodicUpdateService
}