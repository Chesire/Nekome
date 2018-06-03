package com.chesire.malime.view.login.library

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.chesire.malime.kitsu.repositories.KitsuLibrary
import java.io.InvalidClassException

class PrimeLibraryViewModelFactory(
    private val application: Application,
    private val kitsuLibrary: KitsuLibrary
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(PrimeLibraryViewModel::class.java) -> return PrimeLibraryViewModel(
                application,
                kitsuLibrary
            ) as T
            else -> throw InvalidClassException("")
        }
    }
}