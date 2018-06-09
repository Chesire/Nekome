package com.chesire.malime.core.repositories

import android.content.Context
import com.chesire.malime.core.api.MalimeApi
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.room.MalimeDatabase
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class Library(
    context: Context,
    private val malimeApi: MalimeApi
) {
    private val db: MalimeDatabase = MalimeDatabase.getInstance(context)

    fun observeLibrary(): Observable<List<MalimeModel>> {
        return getLibraryFromDb()
    }

    fun updateLibraryFromApi() {
        malimeApi.getUserLibrary()
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    Timber.i("Success")
                    db.malimeDao().insertAll(it)
                },
                {
                    Timber.e(it)
                }
            )
    }

    fun clearLocalLibrary() {
        db.malimeDao().clear()
    }

    private fun getLibraryFromDb(): Observable<List<MalimeModel>> {
        return db.malimeDao().getAll().toObservable()
    }
}