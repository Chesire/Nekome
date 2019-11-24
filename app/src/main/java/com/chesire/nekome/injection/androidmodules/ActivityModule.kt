package com.chesire.nekome.injection.androidmodules

import com.chesire.nekome.flow.Activity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeActivity(): Activity
}
