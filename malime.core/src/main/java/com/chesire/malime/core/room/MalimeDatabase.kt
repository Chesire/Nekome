package com.chesire.malime.core.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import com.chesire.malime.core.SingletonHolder
import com.chesire.malime.core.models.MalimeModel

@Database(entities = [(MalimeModel::class)], version = 1)
@TypeConverters(Converters::class)
abstract class MalimeDatabase : RoomDatabase() {
    abstract fun malimeDao(): MalimeDao

    companion object : SingletonHolder<MalimeDatabase, Context>({
        Room.databaseBuilder(
            it.applicationContext,
            MalimeDatabase::class.java, "malimedatabase.db"
        ).build()
    }) {
        fun clearAllTables(context: Context) {
            this.getInstance(context).clearAllTables()
        }
    }
}