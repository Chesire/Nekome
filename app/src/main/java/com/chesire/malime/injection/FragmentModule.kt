package com.chesire.malime.injection

import com.chesire.malime.view.login.kitsu.KitsuLoginFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeKitsuLoginFragment(): KitsuLoginFragment
}