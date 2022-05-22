package com.chesire.nekome.app.login.syncing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.chesire.nekome.core.Resource
import com.chesire.nekome.core.extensions.postError
import com.chesire.nekome.core.extensions.postSuccess
import com.chesire.nekome.core.flags.AsyncState
import com.chesire.nekome.datasource.series.SeriesRepository
import com.chesire.nekome.datasource.user.User
import com.chesire.nekome.datasource.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ViewModel to aid with pulling down the users series via the [SyncingFragment].
 */
@HiltViewModel
class SyncingViewModel @Inject constructor(
    private val seriesRepo: SeriesRepository,
    userRepo: UserRepository
) : ViewModel() {

    private val _syncStatus = MutableLiveData<AsyncState<Any, Any>>()
    val syncStatus: LiveData<AsyncState<Any, Any>> get() = _syncStatus
    val avatarUrl = userRepo.user
        .map { user ->
            if (user is User.Found) {
                user.domain.avatar.largest?.url
            } else {
                ""
            }
        }.asLiveData()

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
