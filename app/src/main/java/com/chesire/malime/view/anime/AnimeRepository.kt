package com.chesire.malime.view.anime

import com.chesire.malime.mal.api.MalManager
import com.chesire.malime.mal.models.Anime
import com.chesire.malime.mal.room.AnimeDao
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class AnimeRepository(
    private val animeDao: AnimeDao,
    private val malManager: MalManager
) {
    fun getAnime(): Observable<List<Anime>> {
        return getAnimeFromDb()
    }

    private fun getAnimeFromDb(): Observable<List<Anime>> {
        return animeDao.getAll().toObservable()
    }

    /**
     * Update anime in the db using data from the api.
     *
     * @param replace Do a fresh replace of all anime data, will not replace by default
     */
    fun updateAnimeFromApi(replace: Boolean = false) {
        malManager.getAllAnime()
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    if (it.second == null) {
                        clearAnimeInDb()
                    } else {
                        storeAnimeInDb(it.second as List<Anime>, replace)
                    }
                },
                {
                    Timber.e(it, "Null list of anime received from MAL")
                })
    }

    private fun clearAnimeInDb() {
        Observable.fromCallable { animeDao.clear() }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                Timber.d("Clearing anime from the DB")
            }, {
                Timber.e(it, "Failure to clear anime from the DB")
            })
    }

    private fun storeAnimeInDb(anime: List<Anime>, replace: Boolean) {
        Observable.fromCallable {
            if (replace) {
                animeDao.freshInsert(anime)
            } else {
                animeDao.insertAll(anime)
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                Timber.d("Inserted ${anime.size} animes from MAL in DB")
            }, {
                Timber.e(it, "Failure to insert anime from MAL into DB")
            })
    }
}