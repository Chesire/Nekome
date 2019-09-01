package com.chesire.malime.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.chesire.malime.server.flags.Service
import com.chesire.malime.server.models.UserModel

@Dao
interface UserDao {
    @Delete
    suspend fun delete(user: UserModel)

    @Query("DELETE FROM usermodel WHERE service == :service")
    suspend fun delete(service: Service)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserModel)

    @Query("SELECT * FROM usermodel WHERE service == :service")
    suspend fun retrieve(service: Service): UserModel

    @Query("SELECT userId FROM usermodel WHERE service == :service")
    suspend fun retrieveUserId(service: Service): Int?

    @Query("SELECT * FROM usermodel WHERE service == :service")
    fun observe(service: Service): LiveData<UserModel>
}
