package com.chesire.malime.view.maldisplay

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.MutableLiveData
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.repositories.Library
import io.reactivex.BackpressureStrategy
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class MalDisplayViewModel(
    context: Application,
    private val library: Library,
    private val subscribeScheduler: Scheduler,
    private val observeScheduler: Scheduler
) : AndroidViewModel(context), ModelInteractionListener {
    private val disposables = CompositeDisposable()

    val series: LiveData<List<MalimeModel>> = LiveDataReactiveStreams.fromPublisher(
        library.observeLibrary().toFlowable(BackpressureStrategy.ERROR)
    )
    val updateAllStatus = MutableLiveData<UpdatingSeriesStatus>()

    fun checkForLatestSeries() {
        disposables.add(
            library.updateLibraryFromApi()
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .doOnSubscribe {
                    updateAllStatus.value = UpdatingSeriesStatus.Updating
                }
                .doOnError {
                    updateAllStatus.value = UpdatingSeriesStatus.Error
                }
                .doOnComplete {
                    updateAllStatus.value = UpdatingSeriesStatus.Finished
                }
                .subscribe {
                    library.insertIntoLocalLibrary(it)
                }
        )
    }

    override fun onImageClicked(model: MalimeModel) {
        // Make a chrome custom tab
        Timber.d("Series ${model.title} image pressed, loading url")
    }

    override fun updateSeries(
        model: MalimeModel,
        newProgress: Int,
        newStatus: UserSeriesStatus,
        callback: (success: Boolean) -> Unit
    ) {
        Timber.d(
            "Series ${model.title}\n" +
                    "Progress being changed from ${model.progress} to $newProgress\n" +
                    "Status being changed from ${model.userSeriesStatus} to $newStatus"
        )

        disposables.add(
            library.sendUpdateToApi(model, newProgress, newStatus)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe(
                    {
                        library.updateInLocalLibrary(it)
                        callback(true)
                    },
                    {
                        callback(false)
                    }
                )
        )
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}