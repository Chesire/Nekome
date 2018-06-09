package com.chesire.malime.core.repositories

import android.content.Context
import com.chesire.malime.core.api.MalimeApi
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.room.MalimeDatabase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class Library(
    context: Context,
    private val malimeApi: MalimeApi
) {
    private val db: MalimeDatabase = MalimeDatabase.getInstance(context)

    fun observeLibrary(): Observable<List<MalimeModel>> {
        return getLibraryFromDb()
    }

    fun updateLibraryFromApi(): Observable<List<MalimeModel>> {
        return malimeApi.getUserLibrary()
    }

    fun insertIntoLocalLibrary(items: List<MalimeModel>) {
        Completable
            .fromAction({
                db.malimeDao().insertAll(items)
            })
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun clearLocalLibrary() {
        db.malimeDao().clear()
    }

    private fun getLibraryFromDb(): Observable<List<MalimeModel>> {
        return db.malimeDao().getAll().toObservable()
    }
}