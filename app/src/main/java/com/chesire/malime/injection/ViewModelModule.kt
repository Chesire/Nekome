package com.chesire.malime.injection

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.chesire.malime.view.ViewModelFactory
import com.chesire.malime.view.login.kitsu.KitsuLoginViewModel
import com.chesire.malime.view.maldisplay.MalDisplayViewModel
import com.chesire.malime.view.search.SearchViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(KitsuLoginViewModel::class)
    internal abstract fun bindKitsuLoginViewModel(viewModel: KitsuLoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MalDisplayViewModel::class)
    internal abstract fun bindMalDisplayViewModel(viewModel: MalDisplayViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    internal abstract fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}