package com.chesire.nekome.injection.androidmodules

import com.chesire.nekome.flow.Activity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Dagger [Module] for Android Activities.
 */
@Suppress("unused", "UndocumentedPublicFunction")
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeActivity(): Activity
}
