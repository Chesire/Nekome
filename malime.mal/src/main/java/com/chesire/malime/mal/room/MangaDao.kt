package com.chesire.malime.mal.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import android.arch.persistence.room.Update
import com.chesire.malime.mal.models.Manga
import io.reactivex.Flowable

@Dao
interface MangaDao {
    @Query("SELECT * FROM manga")
    fun getAll(): Flowable<List<Manga>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(manga: Manga)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(mangas: List<Manga>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(manga: Manga)

    @Delete
    fun delete(manga: Manga)

    @Transaction
    fun freshInsert(mangas: List<Manga>) {
        clear()
        insertAll(mangas)
    }

    @Query("DELETE FROM manga")
    fun clear()
}