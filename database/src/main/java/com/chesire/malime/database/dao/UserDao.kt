package com.chesire.malime.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chesire.malime.core.flags.Service
import com.chesire.malime.core.models.UserModel

/**
 * Dao to interact with user data.
 */
@Dao
interface UserDao {
    /**
     * Deletes the user [user].
     */
    @Delete
    suspend fun delete(user: UserModel)

    /**
     * Deletes the user based on the [service].
     */
    @Query("DELETE FROM usermodel WHERE service == :service")
    suspend fun delete(service: Service)

    /**
     * Inserts the user [user], replacing it if it already exists.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserModel)

    /**
     * Retrieves the user based on the [service].
     */
    @Query("SELECT * FROM usermodel WHERE service == :service")
    suspend fun retrieve(service: Service): UserModel

    /**
     * Retrieves the userID based on the [service].
     */
    @Query("SELECT userId FROM usermodel WHERE service == :service")
    suspend fun retrieveUserId(service: Service): Int?

    /**
     * Provides an observable for the type of [service].
     */
    @Query("SELECT * FROM usermodel WHERE service == :service")
    fun observe(service: Service): LiveData<UserModel>
}
