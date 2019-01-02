package com.chesire.malime.core.api

import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.MalimeModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue

interface LibraryApi {
    @CheckReturnValue
    fun addItem(item: MalimeModel): Single<MalimeModel>

    @CheckReturnValue
    fun deleteItem(item: MalimeModel): Single<MalimeModel>

    @CheckReturnValue
    fun getUserLibrary(): Observable<List<MalimeModel>>

    @CheckReturnValue
    fun updateItem(
        item: MalimeModel,
        newProgress: Int,
        newStatus: UserSeriesStatus
    ): Single<MalimeModel>
}
