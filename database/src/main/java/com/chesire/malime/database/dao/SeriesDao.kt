package com.chesire.malime.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chesire.malime.core.models.SeriesModel

/**
 * Dao to interact with series data.
 */
@Dao
interface SeriesDao {
    /**
     * Deletes the series [series].
     */
    @Delete
    suspend fun delete(series: SeriesModel)

    /**
     * Inserts the series [series], replacing it if it already exists.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(series: SeriesModel)

    /**
     * Inserts all [series], replacing items where appropriate.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(series: List<SeriesModel>)

    /**
     * Provides an observable for all series.
     */
    @Query("SELECT * FROM seriesmodel")
    fun observe(): LiveData<List<SeriesModel>>

    /**
     * Updates the series [series].
     */
    @Update
    suspend fun update(series: SeriesModel)
}
