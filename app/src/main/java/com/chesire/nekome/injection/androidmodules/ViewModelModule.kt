package com.chesire.nekome.injection.androidmodules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chesire.nekome.ActivityViewModel
import com.chesire.nekome.app.discover.DiscoverViewModel
import com.chesire.nekome.app.login.details.DetailsViewModel
import com.chesire.nekome.app.login.syncing.SyncingViewModel
import com.chesire.nekome.app.profile.ProfileViewModel
import com.chesire.nekome.app.search.SearchViewModel
import com.chesire.nekome.app.search.results.ResultsViewModel
import com.chesire.nekome.app.series.detail.SeriesDetailViewModel
import com.chesire.nekome.app.series.list.SeriesListViewModel
import com.chesire.nekome.core.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

/**
 * Dagger [Module] for Android ViewModels.
 */
@Suppress("unused", "UndocumentedPublicFunction")
@Module
@InstallIn(ApplicationComponent::class)
abstract class ViewModelModule {
    /**
     * Binds the [ViewModelFactory] into the Dagger graph.
     */
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
