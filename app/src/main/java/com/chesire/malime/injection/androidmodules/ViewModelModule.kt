package com.chesire.malime.injection.androidmodules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chesire.malime.app.discover.DiscoverViewModel
import com.chesire.malime.app.profile.ProfileViewModel
import com.chesire.malime.app.search.SearchViewModel
import com.chesire.malime.app.search.results.ResultsViewModel
import com.chesire.malime.app.series.detail.SeriesDetailViewModel
import com.chesire.malime.app.series.list.SeriesListViewModel
import com.chesire.malime.core.viewmodel.ViewModelFactory
import com.chesire.malime.flow.ActivityViewModel
import com.chesire.malime.flow.login.details.DetailsViewModel
import com.chesire.malime.flow.login.syncing.SyncingViewModel
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
    @ViewModelKey(ActivityViewModel::class)
    abstract fun bindActivityViewModel(viewModel: ActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailsViewModel::class)
    abstract fun bindDetailsViewModel(viewModel: DetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DiscoverViewModel::class)
    abstract fun bindDiscoverViewModel(viewModel: DiscoverViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ResultsViewModel::class)
    abstract fun bindResultsViewModel(viewModel: ResultsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SeriesDetailViewModel::class)
    abstract fun bindSeriesDetailViewModel(viewModel: SeriesDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SeriesListViewModel::class)
    abstract fun bindSeriesListViewModel(viewModel: SeriesListViewModel): ViewModel

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
