package com.chesire.nekome.injection.androidmodules

import com.chesire.nekome.app.discover.DiscoverFragment
import com.chesire.nekome.app.profile.ProfileFragment
import com.chesire.nekome.app.search.SearchFragment
import com.chesire.nekome.app.search.results.ResultsFragment
import com.chesire.nekome.app.series.detail.SeriesDetailSheetFragment
import com.chesire.nekome.app.series.list.anime.AnimeFragment
import com.chesire.nekome.app.series.list.manga.MangaFragment
import com.chesire.nekome.app.settings.oss.OssFragment
import com.chesire.nekome.app.timeline.TimelineFragment
import com.chesire.nekome.app.login.details.DetailsFragment
import com.chesire.nekome.app.login.syncing.SyncingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Dagger [Module] for Android Fragments.
 */
@Suppress("unused", "TooManyFunctions", "UndocumentedPublicFunction")
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
