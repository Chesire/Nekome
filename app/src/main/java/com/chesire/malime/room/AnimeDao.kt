package com.chesire.malime.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import android.arch.persistence.room.Update
import com.chesire.malime.mal.models.Anime
import io.reactivex.Flowable

@Dao
interface AnimeDao {
    @Query("SELECT * FROM anime")
    fun getAll(): Flowable<List<Anime>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(anime: Anime)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(animes: List<Anime>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(anime: Anime)

    @Delete
    fun delete(anime: Anime)

    @Delete
    fun deleteAll(animes: List<Anime>)

    @Transaction
    fun freshInsert(animes: List<Anime>) {
        clear()
        insertAll(animes)
    }

    @Query("DELETE FROM anime")
    fun clear()
}