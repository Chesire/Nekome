package com.chesire.malime.kitsu.repositories

import android.content.Context
import com.chesire.malime.core.repositories.Repository
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.kitsu.api.KitsuManager
import com.chesire.malime.kitsu.room.KitsuDatabase
import io.reactivex.Observable
import io.reactivex.Single

class KitsuLibrary(
    context: Context,
    private val kitsuManager: KitsuManager,
    private val kitsuDb: KitsuDatabase = KitsuDatabase.getInstance(context)
): Repository {
    override fun observeLibrary(): Observable<List<MalimeModel>> {
        return getLibraryFromDb()
    }

    override fun updateLibraryFromApi(): Single<Boolean> {
        // need to return something to observe, so can pickup errors
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

    override fun clearLocalLibrary() {
        // dbitem.clear
    }

    private fun getLibraryFromDb(): Observable<List<MalimeModel>> {
        //return kitsuDb.kitsuDao().getAll().toObservable()
    }
}