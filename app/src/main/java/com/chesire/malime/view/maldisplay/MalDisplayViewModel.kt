package com.chesire.malime.view.maldisplay

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.MutableLiveData
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.repositories.Library
import io.reactivex.BackpressureStrategy
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class MalDisplayViewModel(
    context: Application,
    private val library: Library,
    private val subscribeScheduler: Scheduler,
    private val observeScheduler: Scheduler
) : AndroidViewModel(context) {
    private val disposables = CompositeDisposable()

    val series: LiveData<List<MalimeModel>> = LiveDataReactiveStreams.fromPublisher(
        library.observeLibrary().toFlowable(BackpressureStrategy.ERROR)
    )
    val updatingStatus = MutableLiveData<UpdatingSeriesStatus>()

    fun checkForLatestSeries() {
        disposables.add(
            library.updateLibraryFromApi()
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .doOnSubscribe {
                    updatingStatus.value = UpdatingSeriesStatus.Updating
                }
                .doOnError {
                    updatingStatus.value = UpdatingSeriesStatus.Error
                }
                .doOnComplete {
                    updatingStatus.value = UpdatingSeriesStatus.Finished
                }
                .subscribe {
                    library.insertIntoLocalLibrary(it)
                }
        )
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}