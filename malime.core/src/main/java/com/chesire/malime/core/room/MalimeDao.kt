package com.chesire.malime.core.room

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.chesire.malime.core.models.MalimeModel
import io.reactivex.Flowable

@Dao
interface MalimeDao {
    @Query("SELECT * FROM malimemodel")
    fun getAll(): Flowable<List<MalimeModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: MalimeModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<MalimeModel>)

    @Update
    fun update(item: MalimeModel)

    @Query("DELETE FROM malimemodel")
    fun clear()
}