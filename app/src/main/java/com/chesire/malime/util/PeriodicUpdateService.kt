package com.chesire.malime.util

import android.app.job.JobParameters
import android.app.job.JobService
import com.chesire.malime.mal.MalManager
import com.chesire.malime.models.Anime
import com.chesire.malime.models.Manga
import com.chesire.malime.room.MalimeDatabase
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PeriodicUpdateService : JobService() {
    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        val sharedPref = SharedPref(applicationContext)
        val malManager = MalManager(sharedPref.getAuth())

        Timber.i("UpdateService primed, updating anime and manga")
        getLatestAnime(params, sharedPref, malManager)
        getLatestManga(params, sharedPref, malManager)
        return true
    }

    private fun getLatestAnime(
        params: JobParameters?,
        sharedPref: SharedPref,
        malManager: MalManager
    ) {
        malManager.getAllAnime(sharedPref.getUsername())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { result ->
                    Timber.d("Successfully got latest anime from MAL")
                    executeSaveAnimeToLocalDb(params, result.second)
                },
                { _ ->
                    Timber.e("Failed to get latest anime from MAL")
                    jobFinished(params, true)
                }
            )
    }

    private fun getLatestManga(
        params: JobParameters?,
        sharedPref: SharedPref,
        malManager: MalManager
    ) {
        malManager.getAllManga(sharedPref.getUsername())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { result ->
                    Timber.d("Successfully got latest manga from MAL")
                    executeSaveMangaToLocalDb(params, result.second)
                },
                { _ ->
                    Timber.e("Failed to get latest manga from MAL")
                    jobFinished(params, true)
                }
            )
    }

    private fun executeSaveAnimeToLocalDb(params: JobParameters?, animes: List<Anime>) {
        Timber.d("Updating local DB for all anime")

        Completable
            .fromAction({
                MalimeDatabase.getInstance(applicationContext).animeDao().freshInsert(animes)
                jobFinished(params, false)
            })
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    private fun executeSaveMangaToLocalDb(params: JobParameters?, mangas: List<Manga>) {
        Timber.d("Updating local DB for all manga")

        Completable
            .fromAction({
                MalimeDatabase.getInstance(applicationContext).mangaDao().freshInsert(mangas)
                jobFinished(params, false)
            })
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}