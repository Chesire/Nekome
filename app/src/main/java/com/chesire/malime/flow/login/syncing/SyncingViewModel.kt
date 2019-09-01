package com.chesire.malime.flow.login.syncing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.malime.core.flags.AsyncState
import com.chesire.malime.server.Resource
import com.chesire.malime.core.extensions.postError
import com.chesire.malime.core.extensions.postLoading
import com.chesire.malime.core.extensions.postSuccess
import com.chesire.malime.repo.SeriesRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class SyncingViewModel @Inject constructor(
    private val seriesRepo: SeriesRepository
) : ViewModel() {
    private val _syncStatus = MutableLiveData<com.chesire.malime.core.flags.AsyncState<Any, Any>>()
    val syncStatus: LiveData<com.chesire.malime.core.flags.AsyncState<Any, Any>>
        get() = _syncStatus

    fun syncLatestData() = viewModelScope.launch {
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
}
