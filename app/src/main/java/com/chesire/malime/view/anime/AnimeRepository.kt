package com.chesire.malime.view.anime

import com.chesire.malime.mal.MalManager
import com.chesire.malime.models.Anime
import com.chesire.malime.room.AnimeDao
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

    fun updateAnimeFromApi() {
        malManager.getAllAnime()
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    if (it.second != null) {
                        storeAnimeInDb(it.second as List<Anime>)
                    }
                },
                {
                    Timber.e(it, "Null list of anime received from MAL")
                })
    }

    private fun storeAnimeInDb(anime: List<Anime>) {
        Observable.fromCallable { animeDao.insertAll(anime) }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                Timber.d("Inserted ${anime.size} animes from MAL in DB")
            }, {
                Timber.e(it, "Failure to insert anime from MAL into DB")
            })
    }
}