package com.chesire.malime.injection.androidmodules

import com.chesire.malime.LaunchActivity
import com.chesire.malime.view.MainActivity
import com.chesire.malime.view.login.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class MockActivityModule {
    @ContributesAndroidInjector
    internal abstract fun contributeLaunchActivity(): LaunchActivity

    @ContributesAndroidInjector(modules = [MockFragmentModule::class])
    internal abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector(modules = [MockFragmentModule::class])
    internal abstract fun contributeMainActivity(): MainActivity
}
