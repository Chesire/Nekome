package com.chesire.malime.kitsu.repositories

import com.chesire.malime.kitsu.api.KitsuManager
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class KitsuLibrary(
    private val kitsuManager: KitsuManager
) {
    fun updateLibrary() {

        kitsuManager.getUserLibrary()
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    Timber.i("Success")
                },
                {
                    Timber.e("Error")
                })
    }
}