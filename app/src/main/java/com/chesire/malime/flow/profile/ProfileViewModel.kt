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
    val anime = Transformations.map(seriesRepository.anime) { createSeriesProgress(it) }
    val manga = Transformations.map(seriesRepository.manga) { createSeriesProgress(it) }

    private fun createSeriesProgress(items: List<SeriesModel>): SeriesProgress {
        val mapped = items.groupBy { it.userSeriesStatus }
        return SeriesProgress(
            total = items.count().toString(),
            current = mapped.getCountAsString(UserSeriesStatus.Current),
            completed = mapped.getCountAsString(UserSeriesStatus.Completed),
            onHold = mapped.getCountAsString(UserSeriesStatus.OnHold),
            dropped = mapped.getCountAsString(UserSeriesStatus.Dropped),
            planned = mapped.getCountAsString(UserSeriesStatus.Planned),
            unknown = mapped.getCountAsString(UserSeriesStatus.Unknown)
        )
    }

    private fun Map<UserSeriesStatus, List<SeriesModel>>.getCountAsString(
        userStatus: UserSeriesStatus
    ) = this[userStatus]?.count()?.toString() ?: "0"
}
