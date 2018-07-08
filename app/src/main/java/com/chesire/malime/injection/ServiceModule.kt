package com.chesire.malime.injection

import com.chesire.malime.util.updateservice.PeriodicUpdateService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ServiceModule {
    @ContributesAndroidInjector
    internal abstract fun contributePeriodicUpdateService(): PeriodicUpdateService
}