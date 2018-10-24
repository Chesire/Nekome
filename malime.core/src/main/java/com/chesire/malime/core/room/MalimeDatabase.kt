package com.chesire.malime.core.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.chesire.malime.core.models.MalimeModel
import javax.inject.Singleton

@Singleton
@Database(entities = [MalimeModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class MalimeDatabase : RoomDatabase() {
    abstract fun malimeDao(): MalimeDao
}
