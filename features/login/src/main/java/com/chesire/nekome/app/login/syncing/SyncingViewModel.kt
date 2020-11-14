package com.chesire.nekome.app.login.syncing

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.core.extensions.postError
import com.chesire.nekome.core.extensions.postSuccess
import com.chesire.nekome.core.flags.AsyncState
import com.chesire.nekome.library.SeriesRepository
import com.chesire.nekome.server.Resource
import kotlinx.coroutines.launch

/**
 * ViewModel to aid with pulling down the users series via the [SyncingFragment].
 */
class SyncingViewModel @ViewModelInject constructor(
    private val seriesRepo: SeriesRepository,
    userRepo: UserRepository
) : ViewModel() {

    private val _syncStatus = MutableLiveData<AsyncState<Any, Any>>()
    val syncStatus = _syncStatus
    val avatarUrl = Transformations.map(userRepo.user.asLiveData()) { it.avatar.largest?.url }

    /**
     * Sets off the process for pulling down and storing the users series.
     *
     * Success or failure is reported back on [syncStatus].
     */
    fun syncLatestData() = viewModelScope.launch {
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
