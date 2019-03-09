package com.chesire.malime.repo

import androidx.lifecycle.LiveData
import com.chesire.malime.core.flags.SeriesType
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.db.SeriesDao
import javax.inject.Inject

class SeriesRepository @Inject constructor(private val seriesDao: SeriesDao) {
    val anime: LiveData<List<SeriesModel>>
        get() = seriesDao.observe(SeriesType.Anime)

    val manga: LiveData<List<SeriesModel>>
        get() = seriesDao.observe(SeriesType.Manga)
}
