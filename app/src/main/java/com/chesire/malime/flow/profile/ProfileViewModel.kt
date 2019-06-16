package com.chesire.malime.flow.profile

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.repo.SeriesRepository
import com.chesire.malime.repo.UserRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    seriesRepository: SeriesRepository,
    userRepository: UserRepository
) : ViewModel() {
    val user = userRepository.user
    val userName = Transformations.map(user) { it.name }
    val anime = Transformations.map(seriesRepository.anime) { createSeriesProgress(it) }
    val manga = Transformations.map(seriesRepository.manga) { createSeriesProgress(it) }

    // This could be a lot better... fi later
    private fun createSeriesProgress(items: List<SeriesModel>) = SeriesProgress(
        total = items.count().toString(),
        current = items.count { it.userSeriesStatus == UserSeriesStatus.Current }.toString(),
        completed = items.count { it.userSeriesStatus == UserSeriesStatus.Completed }.toString(),
        onHold = items.count { it.userSeriesStatus == UserSeriesStatus.OnHold }.toString(),
        dropped = items.count { it.userSeriesStatus == UserSeriesStatus.Dropped }.toString(),
        planned = items.count { it.userSeriesStatus == UserSeriesStatus.Planned }.toString(),
        unknown = items.count { it.userSeriesStatus == UserSeriesStatus.Unknown }.toString()
    )
}
