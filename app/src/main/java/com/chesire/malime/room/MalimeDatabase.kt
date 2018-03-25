package com.chesire.malime.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.chesire.malime.models.Anime

@Database(entities = [(Anime::class)], version = 1)
abstract class MalimeDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao

    companion object {
        private const val dbName: String = "malime.db"
        private var INSTANCE: MalimeDatabase? = null

        fun getInstance(context: Context): MalimeDatabase? {
            synchronized(MalimeDatabase::class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MalimeDatabase::class.java,
                        dbName
                    ).build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}