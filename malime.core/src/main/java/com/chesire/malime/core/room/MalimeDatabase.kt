package com.chesire.malime.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chesire.malime.core.models.MalimeModel
import javax.inject.Singleton

@Singleton
@Database(entities = [MalimeModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class MalimeDatabase : RoomDatabase() {
    abstract fun malimeDao(): MalimeDao
}
