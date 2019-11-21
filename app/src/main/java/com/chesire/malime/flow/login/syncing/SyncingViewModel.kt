package com.chesire.malime.flow.login.syncing

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chesire.malime.account.UserRepository
import com.chesire.malime.core.extensions.postError
import com.chesire.malime.core.extensions.postSuccess
import com.chesire.malime.core.flags.AsyncState
import com.chesire.malime.series.SeriesRepository
import com.chesire.malime.server.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Provides a ViewModel to aid with the [SyncingFragment].
 */
class SyncingViewModel @Inject constructor(
    private val seriesRepo: SeriesRepository,
    userRepo: UserRepository
) : ViewModel() {
    private val _syncStatus = MutableLiveData<AsyncState<Any, Any>>()
    val syncStatus = _syncStatus
    val avatarUrl = Transformations.map(userRepo.user) { it.avatar.largest?.url }

    /**
     * Sets off the process for pulling down and storing the users series. Success or failure is
     * reported back on [syncStatus].
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
