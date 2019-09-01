package com.chesire.malime.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chesire.malime.core.flags.SeriesType
import com.chesire.malime.core.models.SeriesModel

@Dao
interface SeriesDao {
    @Delete
    suspend fun delete(series: SeriesModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(series: SeriesModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(series: List<SeriesModel>)

    @Query("SELECT * FROM seriesmodel")
    fun observe(): LiveData<List<SeriesModel>>

    @Query("SELECT * FROM seriesmodel WHERE type == :type")
    fun observe(type: SeriesType): LiveData<List<SeriesModel>>

    @Query("SELECT * FROM seriesmodel")
    suspend fun retrieve(): List<SeriesModel>

    @Query("SELECT * FROM seriesmodel WHERE type == :type")
    suspend fun retrieve(type: SeriesType): List<SeriesModel>

    @Update
    suspend fun update(series: SeriesModel)
}
