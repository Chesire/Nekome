package com.chesire.malime.view.maldisplay

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.chesire.malime.core.repositories.Library
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MalDisplayViewModelFactory(
    private val application: Application,
    private val library: Library
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MalDisplayViewModel(
            application,
            library,
            Schedulers.io(),
            AndroidSchedulers.mainThread()
        ) as T
    }
}