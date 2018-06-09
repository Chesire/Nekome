package com.chesire.malime.util

import android.app.job.JobParameters
import android.app.job.JobService
import com.chesire.malime.mal.api.MalManager
import com.chesire.malime.mal.api.MalManagerFactory
import com.chesire.malime.mal.models.Anime
import com.chesire.malime.mal.models.Manga
import com.chesire.malime.mal.room.MalimeDatabase
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class PeriodicUpdateService : JobService() {
    override fun onStopJob(params: JobParameters?): Boolean {
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        val sharedPref = SharedPref(applicationContext)
        val malManager = MalManagerFactory().get(sharedPref.getAuth(), sharedPref.getUsername())

        Timber.i("UpdateService primed, updating anime and manga")
        getLatestAnime(params, malManager)
        getLatestManga(params, malManager)
        return true
    }

    private fun getLatestAnime(
        params: JobParameters?,
        malManager: MalManager
    ) {
        malManager.getAllAnime()
            .subscribeOn(Schedulers.io())
            .subscribe(
                { result ->
                    Timber.d("Successfully got latest anime from MAL")

                    if (result.second == null) {
                        executeClearAnimeInLocalDb(params)
                    } else {
                        executeSaveAnimeToLocalDb(params, result.second as List<Anime>)
                    }
                },
                { _ ->
                    Timber.e("Failed to get latest anime from MAL")
                    jobFinished(params, true)
                }
            )
    }

    private fun getLatestManga(
        params: JobParameters?,
        malManager: MalManager
    ) {
        malManager.getAllManga()
            .subscribeOn(Schedulers.io())
            .subscribe(
                { result ->
                    Timber.d("Successfully got latest manga from MAL")

                    if (result.second == null) {
                        executeClearMangaInLocalDb(params)
                    } else {
                        executeSaveMangaToLocalDb(params, result.second as List<Manga>)
                    }
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

    private fun executeClearAnimeInLocalDb(params: JobParameters?) {
        Timber.d("Clearing local DB for all anime")

        Completable
            .fromAction({
                MalimeDatabase.getInstance(applicationContext).animeDao().clear()
                jobFinished(params, false)
            })
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    private fun executeClearMangaInLocalDb(params: JobParameters?) {
        Timber.d("Clearing local DB for all manga")

        Completable
            .fromAction({
                MalimeDatabase.getInstance(applicationContext).mangaDao().clear()
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