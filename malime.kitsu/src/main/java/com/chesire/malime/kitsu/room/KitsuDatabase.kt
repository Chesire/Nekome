package com.chesire.malime.kitsu.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.chesire.malime.kitsu.SingletonHolder
import com.chesire.malime.kitsu.models.KitsuItem

@Database(entities = [(KitsuItem::class)], version = 1)
abstract class KitsuDatabase : RoomDatabase() {
    abstract fun kitsuDao(): KitsuDao

    companion object : SingletonHolder<KitsuDatabase, Context>({
        Room.databaseBuilder(
            it.applicationContext,
            KitsuDatabase::class.java, "kitsu.db"
        ).build()
    })
}