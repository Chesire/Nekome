package com.chesire.nekome.app.profile

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.chesire.nekome.account.UserRepository
import com.chesire.nekome.dataflags.SeriesType
import com.chesire.nekome.dataflags.UserSeriesStatus
import com.chesire.nekome.library.SeriesDomain
import com.chesire.nekome.library.SeriesRepository

/**
 * ViewModel to provide profile information to [ProfileFragment].
 */
class ProfileViewModel @ViewModelInject constructor(
    seriesRepository: SeriesRepository,
    userRepository: UserRepository
) : ViewModel() {

    val user = userRepository.user.asLiveData()
    val anime = Transformations.map(seriesRepository.getSeries().asLiveData()) {
        createSeriesProgress(it.filter { it.type == SeriesType.Anime })
    }
    val manga = Transformations.map(seriesRepository.getSeries().asLiveData()) {
        createSeriesProgress(it.filter { it.type == SeriesType.Manga })
    }

    private fun createSeriesProgress(items: List<SeriesDomain>): SeriesProgress {
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

    private fun Map<UserSeriesStatus, List<SeriesDomain>>.getCountAsString(
        userStatus: UserSeriesStatus
    ) = this[userStatus]?.count()?.toString() ?: "0"
}
