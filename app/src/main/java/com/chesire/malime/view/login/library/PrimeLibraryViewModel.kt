package com.chesire.malime.view.login.library

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.repositories.Library
import io.reactivex.BackpressureStrategy

class PrimeLibraryViewModel(
    context: Application,
    private val library: Library
) : AndroidViewModel(context) {

    var myLibrary: LiveData<List<MalimeModel>> = LiveDataReactiveStreams.fromPublisher(
        library.observeLibrary().toFlowable(BackpressureStrategy.ERROR)
    )

    fun updateLibrary() {
        // will want to get updates on this, I.E %, number of items etc
        library.updateLibraryFromApi()
    }
}