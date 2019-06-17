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
            current = mapped[UserSeriesStatus.Current].toString(),
            completed = mapped[UserSeriesStatus.Completed].toString(),
            onHold = mapped[UserSeriesStatus.OnHold].toString(),
            dropped = mapped[UserSeriesStatus.Dropped].toString(),
            planned = mapped[UserSeriesStatus.Planned].toString(),
            unknown = mapped[UserSeriesStatus.Unknown].toString()
        )
    }
}
