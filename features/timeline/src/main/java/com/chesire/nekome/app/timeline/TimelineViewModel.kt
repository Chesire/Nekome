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

/**
 * ViewModel for the [TimelineFragment].
 */
@HiltViewModel
class TimelineViewModel @Inject constructor(
    private val activityRepository: ActivityRepository
) : ViewModel() {

    val latestActivity = liveData {
        activityRepository
            .userActivity
            .onStart { emit(ViewState.Loading) }
            .map {
                if (it is UserActivityResult.RetrievedValues) {
                    ViewState.GotActivities(it.newValues)
                } else {
                    ViewState.Failure
                }
            }
            .collect { emit(it) }
    }
}

/**
 * The current state of the view.
 */
sealed class ViewState {

    /**
     * View is in a loading state.
     */
    object Loading : ViewState()

    /**
     * View has got the [ActivityDomain] required to display.
     */
    data class GotActivities(val activities: List<ActivityDomain>) : ViewState()

    /**
     * A failure to retrieve the activity domains has occurred.
     */
    object Failure : ViewState()
}
