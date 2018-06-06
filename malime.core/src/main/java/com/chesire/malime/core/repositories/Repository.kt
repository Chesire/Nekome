package com.chesire.malime.core.repositories

import com.chesire.malime.core.models.MalimeModel
import io.reactivex.Observable
import io.reactivex.Single

interface Repository {
    fun observeLibrary(): Observable<List<MalimeModel>>
    fun updateLibraryFromApi(): Single<Boolean>
    fun clearLocalLibrary()
}