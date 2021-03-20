package com.chesire.nekome.database.dao

import androidx.annotation.VisibleForTesting
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.chesire.nekome.database.entity.SeriesEntity
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
    suspend fun delete(series: SeriesEntity)

    /**
     * Inserts [series], replacing it if it already exists.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(series: SeriesEntity)

    /**
     * Inserts all [series], replacing items where appropriate.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(series: List<SeriesEntity>)

    /**
     * Gets all the [SeriesEntity], and subscribes to updates.
     */
    @Query("SELECT * FROM SeriesEntity")
    fun getSeries(): Flow<List<SeriesEntity>>

    /**
     * Updates the [series].
     */
    @Update
    suspend fun update(series: SeriesEntity)

    // This method only exists to allow easier testing of the SeriesDao.
    /**
     * Retrieves all [SeriesEntity].
     */
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    @Query("SELECT * FROM SeriesEntity")
    suspend fun retrieve(): List<SeriesEntity>
}
