package com.chesire.malime.core.repositories

import android.content.Context
import com.chesire.malime.core.api.MalimeApi
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.room.MalimeDatabase
import io.reactivex.Observable

class Library(
    context: Context,
    private val db: MalimeDatabase = MalimeDatabase.getInstance(context),
    private val malimeApi: MalimeApi
) {
    fun observeLibrary(): Observable<List<MalimeModel>> {
        return getLibraryFromDb()
    }

    fun updateLibraryFromApi() {
        //fun updateLibraryFromApi(): Single<Boolean> {
        // malimeApi.updateLib
        // on new items push into the lib - merge conflicts
        /*

        kitsuManager.getUserLibrary()
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    Timber.i("Success")
                    kitsuDb.kitsuDao().insertAll(it)
                },
                {
                    Timber.e("Error")
                })

         */
    }

    fun clearLocalLibrary() {
        db.malimeDao().clear()
    }

    private fun getLibraryFromDb(): Observable<List<MalimeModel>> {
        return db.malimeDao().getAll().toObservable()
    }
}