package com.chesire.nekome.app.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.chesire.nekome.datasource.activity.ActivityDomain
import com.chesire.nekome.datasource.activity.ActivityRepository
import com.chesire.nekome.datasource.activity.UserActivityResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class TimelineViewModel @Inject constructor(
    private val activityRepository: ActivityRepository
) : ViewModel() {

    val latestActivity = liveData {
        activityRepository
            .userActivity
            .onStart { emit(ActivityState.Loading) }
            .map {
                if (it is UserActivityResult.RetrievedValues) {
                    ActivityState.GotActivities(it.newValues)
                } else {
                    ActivityState.Failure
                }
            }
            .collect { emit(it) }
    }
}

sealed class ActivityState {
    object Loading : ActivityState()
    data class GotActivities(val activities: List<ActivityDomain>) : ActivityState()
    object Failure : ActivityState()
}
