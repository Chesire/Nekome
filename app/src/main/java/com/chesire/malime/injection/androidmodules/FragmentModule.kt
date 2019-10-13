package com.chesire.malime.injection.androidmodules

import com.chesire.malime.app.discover.DiscoverFragment
import com.chesire.malime.app.profile.ProfileFragment
import com.chesire.malime.app.settings.oss.OssFragment
import com.chesire.malime.app.timeline.TimelineFragment
import com.chesire.malime.flow.login.details.DetailsFragment
import com.chesire.malime.flow.login.syncing.SyncingFragment
import com.chesire.malime.app.series.detail.SeriesDetailSheetFragment
import com.chesire.malime.app.series.detail.list.anime.AnimeFragment
import com.chesire.malime.app.series.detail.list.manga.MangaFragment
import com.chesire.malime.app.series.search.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract fun contributeAnimeFragment(): com.chesire.malime.app.series.detail.list.anime.AnimeFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailsFragment(): DetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeDiscoverFragment(): DiscoverFragment

    @ContributesAndroidInjector
    abstract fun contributeMangaFragment(): com.chesire.malime.app.series.detail.list.manga.MangaFragment

    @ContributesAndroidInjector
    abstract fun contributeOssFragment(): OssFragment

    @ContributesAndroidInjector
    abstract fun contributeProfileFragment(): ProfileFragment

    @Suppress("FunctionMaxLength")
    @ContributesAndroidInjector
    abstract fun contributeSeriesDetailSheetFragment(): com.chesire.malime.app.series.detail.SeriesDetailSheetFragment

    @ContributesAndroidInjector
    abstract fun contributeSeriesSearchFragment(): com.chesire.malime.app.series.search.SearchFragment

    @ContributesAndroidInjector
    abstract fun contributeSyncingFragment(): SyncingFragment

    @ContributesAndroidInjector
    abstract fun contributeTimelineFragment(): TimelineFragment
}
