package com.chesire.malime.core.repositories

import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.room.MalimeDao
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class Library @Inject constructor(
    private val libraryApi: LibraryApi,
    private val dao: MalimeDao
) {
    fun observeLibrary(): Observable<List<MalimeModel>> = getLibraryFromDb()

    fun updateLibraryFromApi(): Observable<List<MalimeModel>> = libraryApi.getUserLibrary()

    fun sendNewToApi(item: MalimeModel): Single<MalimeModel> = libraryApi.addItem(item)

    fun sendUpdateToApi(
        item: MalimeModel,
        newProgress: Int,
        newStatus: UserSeriesStatus
    ): Single<MalimeModel> = libraryApi.updateItem(item, newProgress, newStatus)

    fun sendDeleteToApi(item: MalimeModel): Single<MalimeModel> = libraryApi.deleteItem(item)

    fun deleteFromLocalLibrary(item: MalimeModel) {
        Completable
            .fromAction { dao.delete(item) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun insertIntoLocalLibrary(item: MalimeModel) {
        Completable
            .fromAction { dao.insert(item) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun insertIntoLocalLibrary(items: List<MalimeModel>) {
        Completable
            .fromAction { dao.insertAll(items) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun updateInLocalLibrary(item: MalimeModel) {
        Completable
            .fromAction { dao.update(item) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    private fun getLibraryFromDb(): Observable<List<MalimeModel>> = dao.getAll().toObservable()
}
