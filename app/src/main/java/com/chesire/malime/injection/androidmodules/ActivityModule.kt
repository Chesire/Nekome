package com.chesire.malime.injection.androidmodules

import com.chesire.malime.flow.Activity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeActivity(): Activity
}
