package com.chesire.malime.view.login.library

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import com.chesire.malime.kitsu.models.KitsuItem
import com.chesire.malime.kitsu.repositories.KitsuLibrary
import io.reactivex.BackpressureStrategy

class PrimeLibraryViewModel(
    context: Application,
    private val kitsuLibrary: KitsuLibrary
) : AndroidViewModel(context) {

    var myLibrary: LiveData<List<KitsuItem>> = LiveDataReactiveStreams.fromPublisher(
        kitsuLibrary.getLibrary().toFlowable(BackpressureStrategy.ERROR)
    )

    fun updateLibrary() {
        // will want to get updates on this, I.E %, number of items etc
        kitsuLibrary.updateLibrary()
    }
}