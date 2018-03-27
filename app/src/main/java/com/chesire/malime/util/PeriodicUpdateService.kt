package com.chesire.malime.util

import android.app.job.JobParameters
import android.app.job.JobService
import com.chesire.malime.mal.MalManager
import com.chesire.malime.models.Anime
import com.chesire.malime.room.MalimeDatabase
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PeriodicUpdateService : JobService() {
    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        getLatestAnime(params)
        return true
    }

    private fun getLatestAnime(params: JobParameters?) {
        val sharedPref = SharedPref(applicationContext)
        val malManager = MalManager(sharedPref.getAuth())

        malManager.getAllAnime(sharedPref.getUsername())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result ->
                    Timber.d("Successfully got latest anime from MAL")
                    executeSaveToLocalDb(params, result.second)
                },
                { _ ->
                    Timber.e("Failed to get latest anime from MAL")
                    jobFinished(params, true)
                }
            )
    }

    private fun executeSaveToLocalDb(params: JobParameters?, animes: List<Anime>) {
        Timber.d("Updating local DB for all anime")

        // Looks like this doesn't have to be disposed off
        Completable
            .fromAction({
                MalimeDatabase.getInstance(applicationContext).animeDao().insertAll(animes)
                jobFinished(params, false)
            })
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}