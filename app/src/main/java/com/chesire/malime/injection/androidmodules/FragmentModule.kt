package com.chesire.malime.injection.androidmodules

import com.chesire.malime.flow.activity.ActivityFragment
import com.chesire.malime.flow.login.details.DetailsFragment
import com.chesire.malime.flow.login.syncing.SyncingFragment
import com.chesire.malime.flow.profile.ProfileFragment
import com.chesire.malime.flow.series.detail.SeriesDetailFragment
import com.chesire.malime.flow.series.list.anime.AnimeFragment
import com.chesire.malime.flow.series.list.manga.MangaFragment
import com.chesire.malime.flow.series.search.SearchFragment
import com.chesire.malime.flow.settings.SettingsFragment
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
    abstract fun contributeDetailsFragment(): DetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeMangaFragment(): MangaFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeSettingsFragment(): SettingsFragment

    @ContributesAndroidInjector
    abstract fun contributeSeriesDetailFragment(): SeriesDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeSeriesSearchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributeSyncingFragment(): SyncingFragment
}
