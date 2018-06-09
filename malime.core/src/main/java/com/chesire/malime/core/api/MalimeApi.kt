package com.chesire.malime.core.api

import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.LoginResponse
import com.chesire.malime.core.models.MalimeModel
import io.reactivex.Observable
import io.reactivex.Single

interface MalimeApi {
    fun login(username: String, password: String): Single<LoginResponse>
    fun getUserId(username: String): Single<Int>
    fun getUserLibrary(): Observable<List<MalimeModel>>
    fun updateItem(
        item: MalimeModel,
        newProgress: Int,
        newStatus: UserSeriesStatus
    ): Single<MalimeModel>
}