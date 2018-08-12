package com.chesire.malime.mocks

import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.models.MalimeModel
import io.reactivex.Observable
import javax.inject.Inject

class MockSearchApi @Inject constructor() : SearchApi {
    override fun searchForSeriesWith(title: String, type: ItemType): Observable<List<MalimeModel>> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}