package com.chesire.malime.injection

import com.chesire.malime.view.login.kitsu.KitsuLoginFragment
import com.chesire.malime.view.login.mal.MalLoginFragment
import com.chesire.malime.view.search.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeKitsuLoginFragment(): KitsuLoginFragment

    @ContributesAndroidInjector
    abstract fun contributeMalLoginFragment(): MalLoginFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment
}