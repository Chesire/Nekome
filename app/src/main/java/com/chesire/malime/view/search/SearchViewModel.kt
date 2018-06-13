package com.chesire.malime.view.search

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.MutableLiveData
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.repositories.Library
import io.reactivex.BackpressureStrategy
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber

class SearchViewModel(
    context: Application,
    private val searchApi: SearchApi,
    private val library: Library,
    private val subscribeScheduler: Scheduler,
    private val observeScheduler: Scheduler
) : AndroidViewModel(context), Search2InteractionListener {
    private val disposables = CompositeDisposable()
    val series: LiveData<List<MalimeModel>> = LiveDataReactiveStreams.fromPublisher(
        library.observeLibrary().toFlowable(BackpressureStrategy.ERROR)
    )
    val searchItems = MutableLiveData<List<MalimeModel>>()
    val params = SearchParams()

    fun searchForSeries(type: ItemType) {
        if (params.searchText.isBlank() && type == ItemType.Unknown) {
            Timber.w("No text entered or type was unknown")
            return
        }

        disposables.add(
            searchApi.searchForSeriesWith(params.searchText, type)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .doOnSubscribe {
                    params.searching = true
                }
                .doOnComplete {
                    params.searching = false
                }
                .doOnError {
                    Timber.e(it, "Error performing the search")
                    params.searching = false
                }
                .subscribe {
                    Timber.i("Found ${it.count()} items")
                    searchItems.value = it
                }
        )
    }

    override fun addNewSeries(selectedSeries: MalimeModel, callback: (Boolean) -> Unit) {
        disposables.add(
            library.sendNewToApi(selectedSeries)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe(
                    {
                        library.insertIntoLocalLibrary(it)
                        callback(true)
                    },
                    {
                        callback(false)
                    }
                )
        )
    }

    override fun navigateToSeries(selectedSeries: MalimeModel) {
        Timber.d("Series ${selectedSeries.title} image pressed, loading url")
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(getApplication(), Uri.parse(library.getItemUrl(selectedSeries)))
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}