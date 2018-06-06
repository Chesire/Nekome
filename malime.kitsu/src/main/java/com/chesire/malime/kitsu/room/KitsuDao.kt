package com.chesire.malime.kitsu.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.chesire.malime.kitsu.models.KitsuItem
import io.reactivex.Flowable

@Dao
interface KitsuDao {
    @Query("SELECT * FROM kitsuItem")
    fun getAll(): Flowable<List<KitsuItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<KitsuItem>)
}