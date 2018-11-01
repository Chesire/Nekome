package com.chesire.malime.core.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chesire.malime.core.models.MalimeModel
import io.reactivex.Flowable

@Dao
interface MalimeDao {
    @Delete
    fun delete(item: MalimeModel)

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
