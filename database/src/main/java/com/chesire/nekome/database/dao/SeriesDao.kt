package com.chesire.nekome.database.dao

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chesire.nekome.core.models.SeriesModel
import kotlinx.coroutines.flow.Flow

/**
 * Dao to interact with series data.
 */
@Dao
interface SeriesDao {
    /**
     * Deletes [series] from the dao.
     */
    @Delete
    suspend fun delete(series: SeriesModel)

    /**
     * Inserts [series], replacing it if it already exists.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(series: SeriesModel)

    /**
     * Inserts all [series], replacing items where appropriate.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(series: List<SeriesModel>)

    /**
     * Gets all the [SeriesModel], and subscribes to updates.
     */
    @Query("SELECT * FROM seriesmodel")
    fun getSeries(): Flow<List<SeriesModel>>

    /**
     * Updates the [series].
     */
    @Update
    suspend fun update(series: SeriesModel)

    // This method only exists to allow easier testing of the SeriesDao.
    /**
     * Retrieves all [SeriesModel].
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Query("SELECT * FROM seriesmodel")
    suspend fun retrieve(): List<SeriesModel>
}
