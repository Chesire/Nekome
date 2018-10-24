package com.chesire.malime.view.maldisplay

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.repositories.Library
import com.chesire.malime.util.IOScheduler
import com.chesire.malime.util.UIScheduler
import io.reactivex.BackpressureStrategy
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

class MalDisplayViewModel @Inject constructor(
    private val library: Library
) : ViewModel() {
    private val disposables = CompositeDisposable()

    @Inject
    @field:IOScheduler
    lateinit var subscribeScheduler: Scheduler

    @Inject
    @field:UIScheduler
    lateinit var observeScheduler: Scheduler

    val series: LiveData<List<MalimeModel>> = LiveDataReactiveStreams.fromPublisher(
        library.observeLibrary().toFlowable(BackpressureStrategy.ERROR)
    )
    val updateAllStatus = MutableLiveData<UpdatingSeriesStatus>()

    fun checkForLatestSeries() {
        disposables.add(
            library.updateLibraryFromApi()
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .doOnSubscribe { updateAllStatus.value = UpdatingSeriesStatus.Updating }
                .subscribeBy(
                    onError = { updateAllStatus.value = UpdatingSeriesStatus.Error },
                    onNext = { library.insertIntoLocalLibrary(it) },
                    onComplete = { updateAllStatus.value = UpdatingSeriesStatus.Finished }
                )
        )
    }

    fun deleteSeries(model: MalimeModel, callback: (success: Boolean) -> Unit) {
        Timber.d("Series ${model.title} is being deleted")

        disposables.add(
            library.sendDeleteToApi(model)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribeBy(
                    onError = { callback(false) },
                    onSuccess = {
                        library.deleteFromLocalLibrary(it)
                        callback(true)
                    }
                )
        )
    }

    fun updateSeries(
        model: MalimeModel,
        newProgress: Int,
        newStatus: UserSeriesStatus,
        callback: (success: Boolean) -> Unit
    ) {
        Timber.d("Series ${model.title}\nProgress being changed from ${model.progress} to $newProgress\nStatus being changed from ${model.userSeriesStatus} to $newStatus")

        disposables.add(
            library.sendUpdateToApi(model, newProgress, newStatus)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribeBy(
                    onError = { callback(false) },
                    onSuccess = {
                        library.updateInLocalLibrary(it)
                        callback(true)
                    }
                )
        )
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}
