package com.chesire.malime.injection

import android.arch.lifecycle.ViewModel
import com.chesire.malime.view.login.kitsu.KitsuLoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {
    // currently needs more done to it, for this method to work
    @Binds
    @IntoMap
    @ViewModelKey(KitsuLoginViewModel::class)
    abstract fun bindKitsuLoginViewModel(viewModel: KitsuLoginViewModel): ViewModel
}