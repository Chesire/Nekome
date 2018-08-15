package com.chesire.malime.core.api

import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.models.MalimeModel
import io.reactivex.Single

interface SearchApi {
    fun searchForSeriesWith(title: String, type: ItemType): Single<List<MalimeModel>>
}