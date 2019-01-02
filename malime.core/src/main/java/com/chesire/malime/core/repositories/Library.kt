package com.chesire.malime.core.repositories

import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.room.MalimeDao
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class Library @Inject constructor(
    private val libraryApi: LibraryApi,
    private val dao: MalimeDao
) {
    fun observeLibrary(): Observable<List<MalimeModel>> = getLibraryFromDb()

    fun updateLibraryFromApi(): Observable<List<MalimeModel>> = libraryApi.getUserLibrary()

    @CheckReturnValue
    fun sendNewToApi(item: MalimeModel): Single<MalimeModel> = libraryApi.addItem(item)

    @CheckReturnValue
    fun sendUpdateToApi(
        item: MalimeModel,
        newProgress: Int,
        newStatus: UserSeriesStatus
    ): Single<MalimeModel> = libraryApi.updateItem(item, newProgress, newStatus)

    @CheckReturnValue
    fun sendDeleteToApi(item: MalimeModel): Single<MalimeModel> = libraryApi.deleteItem(item)

    fun deleteFromLocalLibrary(item: MalimeModel) {
        Completable
            .fromAction { dao.delete(item) }
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = {
                    Timber.e(it, "Failure to delete $item from library")
                }
            )
    }

    fun insertIntoLocalLibrary(item: MalimeModel) {
        Completable
            .fromAction { dao.insert(item) }
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = {
                    Timber.e(it, "Failure to insert $item into library")
                }
            )
    }

    fun insertIntoLocalLibrary(items: List<MalimeModel>) {
        Completable
            .fromAction { dao.insertAll(items) }
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = {
                    Timber.e(it, "Failure to insert items into library")
                }
            )
    }

    fun updateInLocalLibrary(item: MalimeModel) {
        Completable
            .fromAction { dao.update(item) }
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = {
                    Timber.e(it, "Failure to update $item in library")
                }
            )
    }

    private fun getLibraryFromDb(): Observable<List<MalimeModel>> = dao.getAll().toObservable()
}
