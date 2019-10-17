package com.chesire.malime.injection.androidmodules

import com.chesire.malime.app.discover.DiscoverFragment
import com.chesire.malime.app.profile.ProfileFragment
import com.chesire.malime.app.search.SearchFragment
import com.chesire.malime.app.search.results.ResultsFragment
import com.chesire.malime.app.series.detail.SeriesDetailSheetFragment
import com.chesire.malime.app.series.list.anime.AnimeFragment
import com.chesire.malime.app.series.list.manga.MangaFragment
import com.chesire.malime.app.settings.oss.OssFragment
import com.chesire.malime.app.timeline.TimelineFragment
import com.chesire.malime.flow.login.details.DetailsFragment
import com.chesire.malime.flow.login.syncing.SyncingFragment
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
    abstract fun contributeDiscoverFragment(): DiscoverFragment

    @ContributesAndroidInjector
    abstract fun contributeMangaFragment(): MangaFragment

    @ContributesAndroidInjector
    abstract fun contributeOssFragment(): OssFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment

    @ContributesAndroidInjector
    abstract fun contributeResultsFragment(): ResultsFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment

    @Suppress("FunctionMaxLength")
    @ContributesAndroidInjector
    abstract fun contributeSeriesDetailSheetFragment(): SeriesDetailSheetFragment

    @ContributesAndroidInjector
    abstract fun contributeSyncingFragment(): SyncingFragment

    @ContributesAndroidInjector
    abstract fun contributeTimelineFragment(): TimelineFragment
}
