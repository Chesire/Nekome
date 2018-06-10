package com.chesire.malime.view.search

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.databinding.Bindable
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.repositories.Library
import io.reactivex.BackpressureStrategy
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class SearchViewModel(
    context: Application,
    private val searchApi: SearchApi,
    private val library: Library,
    private val subscribeScheduler: Scheduler,
    private val observeScheduler: Scheduler
) : AndroidViewModel(context) {
    private val disposables = CompositeDisposable()
    val series: LiveData<List<MalimeModel>> = LiveDataReactiveStreams.fromPublisher(
        library.observeLibrary().toFlowable(BackpressureStrategy.ERROR)
    )

    @Bindable
    var searchText = ""

    fun searchForSeries(title: String, type: ItemType) {
    }

    fun addSeries(item: MalimeModel) {

    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}