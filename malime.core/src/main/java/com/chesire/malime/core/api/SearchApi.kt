package com.chesire.malime.core.api

import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.models.MalimeModel
import io.reactivex.Observable

interface SearchApi {
    fun searchForSeriesWith(name: String, type: ItemType): Observable<List<MalimeModel>>
}