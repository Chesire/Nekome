package com.chesire.malime.flow.series.list

import com.chesire.malime.server.models.SeriesModel

interface SeriesListener {
    fun loadDetailFragment(seriesModel: SeriesModel)
    fun loadSearchFragment()
}
