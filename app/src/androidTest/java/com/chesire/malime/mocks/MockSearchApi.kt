package com.chesire.malime.mocks

import com.chesire.malime.INVALID_SEARCH
import com.chesire.malime.INVALID_SEARCH_NO_ITEMS
import com.chesire.malime.VALID_SEARCH
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.models.MalimeModel
import io.reactivex.Observable
import javax.inject.Inject

class MockSearchApi @Inject constructor() : SearchApi {
    override fun searchForSeriesWith(title: String, type: ItemType): Observable<List<MalimeModel>> {
        return Observable.create {
            when (title) {
                INVALID_SEARCH -> it.tryOnError(Throwable("Invalid search supplied"))
                INVALID_SEARCH_NO_ITEMS -> {
                    it.onNext(listOf())
                    it.onComplete()
                }
                VALID_SEARCH -> {
                    // do some stuff
                }
            }
        }
    }
}