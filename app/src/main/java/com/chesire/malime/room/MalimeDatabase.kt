package com.chesire.malime.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.chesire.malime.models.Anime
import com.chesire.malime.util.SingletonHolder

@Database(entities = [(Anime::class)], version = 1)
abstract class MalimeDatabase : RoomDatabase() {
    abstract fun animeDao(): AnimeDao

    companion object : SingletonHolder<MalimeDatabase, Context>({
        Room.databaseBuilder(
            it.applicationContext,
            MalimeDatabase::class.java, "malime.db"
        ).build()
    })
}