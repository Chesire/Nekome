package com.chesire.malime.flow.series.list

import com.chesire.malime.core.models.SeriesModel

interface SeriesListener {
    fun loadDetailFragment(seriesModel: SeriesModel)
    fun loadSearchFragment()
}
