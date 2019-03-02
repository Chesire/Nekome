package com.chesire.malime.injection.androidmodules

import com.chesire.malime.LaunchActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    internal abstract fun contributeLaunchActivity(): LaunchActivity
}
