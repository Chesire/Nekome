package com.chesire.malime.injection.androidmodules

import com.chesire.malime.flow.login.details.DetailsFragment
import com.chesire.malime.flow.login.syncing.SyncingFragment
import com.chesire.malime.flow.profile.ProfileFragment
import com.chesire.malime.flow.series.detail.SeriesDetailSheetFragment
import com.chesire.malime.flow.series.list.anime.AnimeFragment
import com.chesire.malime.flow.series.list.manga.MangaFragment
import com.chesire.malime.flow.series.search.SearchFragment
import com.chesire.malime.flow.settings.OssFragment
import com.chesire.malime.flow.timeline.TimelineFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeAnimeFragment(): AnimeFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailsFragment(): DetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeMangaFragment(): MangaFragment

    @ContributesAndroidInjector
    abstract fun contributeOssFragment(): OssFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeSeriesDetailSheetFragment(): SeriesDetailSheetFragment

    @ContributesAndroidInjector
    abstract fun contributeSeriesSearchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributeSyncingFragment(): SyncingFragment

    @ContributesAndroidInjector
    abstract fun contributeTimelineFragment(): TimelineFragment
}
