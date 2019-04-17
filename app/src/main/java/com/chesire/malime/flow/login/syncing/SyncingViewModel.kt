package com.chesire.malime.flow.login.syncing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.chesire.malime.AsyncState
import com.chesire.malime.IOContext
import com.chesire.malime.core.Resource
import com.chesire.malime.extensions.postError
import com.chesire.malime.extensions.postLoading
import com.chesire.malime.extensions.postSuccess
import com.chesire.malime.repo.SeriesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SyncingViewModel @Inject constructor(
    private val seriesRepo: SeriesRepository,
    @IOContext private val ioContext: CoroutineContext
) : ViewModel() {
    private val job = Job()
    private val ioScope = CoroutineScope(job + ioContext)
    private val _syncStatus = MutableLiveData<AsyncState<Any, Any>>()
    val syncStatus: LiveData<AsyncState<Any, Any>>
        get() = _syncStatus

    fun syncLatestData() = ioScope.launch {
        _syncStatus.postLoading()

        val syncCommands = listOf(
            seriesRepo.refreshAnime(),
            seriesRepo.refreshManga()
        )

        if (syncCommands.any { it is Resource.Error }) {
            _syncStatus.postError(Any())
        } else {
            _syncStatus.postSuccess(Any())
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
