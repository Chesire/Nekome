package com.chesire.malime.injection.androidmodules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chesire.malime.flow.ViewModelFactory
import com.chesire.malime.flow.login.details.DetailsViewModel
import com.chesire.malime.flow.login.syncing.SyncingViewModel
import com.chesire.malime.flow.oob.AnalyticsViewModel
import com.chesire.malime.flow.series.detail.SeriesDetailViewModel
import com.chesire.malime.flow.series.list.anime.AnimeViewModel
import com.chesire.malime.flow.series.search.SearchViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    abstract fun bindDetailsViewModel(viewModel: DetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AnalyticsViewModel::class)
    abstract fun bindAnalyticsViewModel(viewModel: AnalyticsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AnimeViewModel::class)
    abstract fun bindAnimeViewModel(viewModel: AnimeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SeriesDetailViewModel::class)
    abstract fun bindSeriesDetailViewModel(viewModel: SeriesDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SyncingViewModel::class)
    abstract fun bindSyncingViewModel(viewModel: SyncingViewModel): ViewModel

    @Target(
        AnnotationTarget.FUNCTION,
        AnnotationTarget.PROPERTY_GETTER,
        AnnotationTarget.PROPERTY_SETTER
    )
    @MapKey
    private annotation class ViewModelKey(val value: KClass<out ViewModel>)
}
