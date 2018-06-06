package com.chesire.malime.kitsu.repositories

import android.content.Context
import com.chesire.malime.kitsu.api.KitsuManager
import com.chesire.malime.kitsu.models.KitsuItem
import com.chesire.malime.kitsu.room.KitsuDatabase
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class KitsuLibrary(
    context: Context,
    private val kitsuManager: KitsuManager,
    private val kitsuDb: KitsuDatabase = KitsuDatabase.getInstance(context)
) {
    fun getLibrary(): Observable<List<KitsuItem>> {
        return kitsuDb.kitsuDao().getAll().toObservable()
    }

    fun updateLibrary() {
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
    }
}