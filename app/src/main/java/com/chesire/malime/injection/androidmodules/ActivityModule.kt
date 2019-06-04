package com.chesire.malime.injection.androidmodules

import com.chesire.malime.flow.LaunchActivity
import com.chesire.malime.flow.OverviewActivity
import com.chesire.malime.flow.SetupActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract fun contributeLaunchActivity(): LaunchActivity

    @ContributesAndroidInjector
    abstract fun contributeOverviewActivity(): OverviewActivity

    @ContributesAndroidInjector
    abstract fun contributeLoginActivity(): SetupActivity
}
