package com.chesire.malime.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chesire.malime.core.models.SeriesModel

@Dao
interface SeriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(series: SeriesModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(series: List<SeriesModel>)

    @Query("SELECT * FROM seriesmodel")
    suspend fun get(): List<SeriesModel>
}
