package com.chesire.malime.flow.profile

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.repo.SeriesRepository
import com.chesire.malime.repo.UserRepository
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    seriesRepository: SeriesRepository,
    userRepository: UserRepository
) : ViewModel() {
    val user = userRepository.user
    val anime = Transformations.map(seriesRepository.anime) { series ->
        SeriesProgress(
            total = series.count(),
            current = series.count { it.userSeriesStatus == UserSeriesStatus.Current },
            completed = series.count { it.userSeriesStatus == UserSeriesStatus.Completed },
            onHold = series.count { it.userSeriesStatus == UserSeriesStatus.OnHold },
            dropped = series.count { it.userSeriesStatus == UserSeriesStatus.Dropped },
            planned = series.count { it.userSeriesStatus == UserSeriesStatus.Planned },
            unknown = series.count { it.userSeriesStatus == UserSeriesStatus.Unknown }
        )
    }
    val manga = Transformations.map(seriesRepository.manga) { series ->
        SeriesProgress(
            total = series.count(),
            current = series.count { it.userSeriesStatus == UserSeriesStatus.Current },
            completed = series.count { it.userSeriesStatus == UserSeriesStatus.Completed },
            onHold = series.count { it.userSeriesStatus == UserSeriesStatus.OnHold },
            dropped = series.count { it.userSeriesStatus == UserSeriesStatus.Dropped },
            planned = series.count { it.userSeriesStatus == UserSeriesStatus.Planned },
            unknown = series.count { it.userSeriesStatus == UserSeriesStatus.Unknown }
        )
    }

    val userName = Transformations.map(user) { it.name }
}
