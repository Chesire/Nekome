package com.chesire.malime.injection.androidmodules

import com.chesire.malime.flow.login.LoginFragment
import com.chesire.malime.flow.overview.activity.ActivityFragment
import com.chesire.malime.flow.overview.anime.AnimeFragment
import com.chesire.malime.flow.overview.manga.MangaFragment
import com.chesire.malime.flow.overview.profile.ProfileFragment
import com.chesire.malime.flow.overview.settings.SettingsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeActivityFragment(): ActivityFragment

    @ContributesAndroidInjector
    abstract fun contributeAnimeFragment(): AnimeFragment

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeMangaFragment(): MangaFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment
}
