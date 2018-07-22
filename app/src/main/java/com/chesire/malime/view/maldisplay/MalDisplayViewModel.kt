package com.chesire.malime.view.maldisplay

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.repositories.Library
import com.chesire.malime.util.IOScheduler
import com.chesire.malime.util.UIScheduler
import io.reactivex.BackpressureStrategy
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

class MalDisplayViewModel @Inject constructor(
    context: Application,
    private val library: Library
) : AndroidViewModel(context), ModelInteractionListener {
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

    override fun showSeriesProfile(model: MalimeModel) {
        Timber.d("Series ${model.title} image pressed, loading url")
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(getApplication(), Uri.parse(library.getItemUrl(model)))
    }

    override fun updateSeries(
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