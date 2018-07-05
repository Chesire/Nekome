package com.chesire.malime.injection

import com.chesire.malime.LaunchActivity
import com.chesire.malime.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    internal abstract fun contributeLaunchActivity(): LaunchActivity

    @ContributesAndroidInjector
    internal abstract fun contributeMainActivity(): MainActivity
}