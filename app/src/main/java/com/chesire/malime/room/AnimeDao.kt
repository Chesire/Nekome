package com.chesire.malime.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.chesire.malime.models.Anime

@Dao
interface AnimeDao {
    @Query("SELECT * FROM anime")
    fun getAll(): List<Anime>

    @Insert
    fun insertAll(animes: List<Anime>)

    @Delete
    fun delete(anime: Anime)
}