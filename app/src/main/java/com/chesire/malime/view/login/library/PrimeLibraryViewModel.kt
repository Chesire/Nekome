package com.chesire.malime.view.login.library

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import com.chesire.malime.kitsu.repositories.KitsuLibrary

class PrimeLibraryViewModel(
    private val context: Application,
    private val kitsuLibrary: KitsuLibrary
) : AndroidViewModel(context) {

    fun updateLibrary() {
        // will want to get updates on this, I.E %, number of items etc
        kitsuLibrary.updateLibrary()
    }
}