package com.chesire.nekome.app.profile

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.UserSeriesStatus
import com.chesire.nekome.datasource.series.SeriesDomain
import com.chesire.nekome.datasource.series.SeriesRepository
import com.chesire.nekome.datasource.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel to provide profile information to [ProfileFragment].
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
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
