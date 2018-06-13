package com.chesire.malime.core.repositories

import android.content.Context
import com.chesire.malime.core.api.MalimeApi
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.room.MalimeDatabase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class Library(
    context: Context,
    private val malimeApi: MalimeApi
) {
    private val db = MalimeDatabase.getInstance(context)

    fun observeLibrary(): Observable<List<MalimeModel>> {
        return getLibraryFromDb()
    }

    fun updateLibraryFromApi(): Observable<List<MalimeModel>> {
        return malimeApi.getUserLibrary()
    }

    fun sendUpdateToApi(
        item: MalimeModel,
        newProgress: Int,
        newStatus: UserSeriesStatus
    ): Single<MalimeModel> {
        return malimeApi.updateItem(item, newProgress, newStatus)
    }

    fun insertIntoLocalLibrary(items: List<MalimeModel>) {
        Completable
            .fromAction {
                db.malimeDao().insertAll(items)
            }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun updateInLocalLibrary(item: MalimeModel) {
        Completable
            .fromAction {
                db.malimeDao().update(item)
            }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun clearLocalLibrary() {
        db.malimeDao().clear()
    }

    fun getItemUrl(item: MalimeModel): String {
        return malimeApi.getItemUrl(item)
    }

    private fun getLibraryFromDb(): Observable<List<MalimeModel>> {
        return db.malimeDao().getAll().toObservable()
    }
}