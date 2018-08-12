package com.chesire.malime.mocks

import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.MalimeModel
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class MockLibraryApi @Inject constructor() : LibraryApi {
    override fun addItem(item: MalimeModel): Single<MalimeModel> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteItem(item: MalimeModel): Single<MalimeModel> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserLibrary(): Observable<List<MalimeModel>> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun updateItem(
        item: MalimeModel,
        newProgress: Int,
        newStatus: UserSeriesStatus
    ): Single<MalimeModel> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}