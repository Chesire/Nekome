package com.chesire.malime.injection.androidmodules

import com.chesire.malime.view.login.kitsu.KitsuLoginFragment
import com.chesire.malime.view.login.mal.MalLoginFragment
import com.chesire.malime.view.maldisplay.MalDisplayFragment
import com.chesire.malime.view.search.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    internal abstract fun contributeKitsuLoginFragment(): KitsuLoginFragment

    @ContributesAndroidInjector
    internal abstract fun contributeMalDisplayFragment(): MalDisplayFragment

    @ContributesAndroidInjector
    internal abstract fun contributeMalLoginFragment(): MalLoginFragment

    @ContributesAndroidInjector
    internal abstract fun contributeSearchFragment(): SearchFragment
}