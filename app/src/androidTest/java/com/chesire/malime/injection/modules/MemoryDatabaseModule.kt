package com.chesire.malime.injection.modules

import android.content.Context
import androidx.room.Room
import com.chesire.malime.db.RoomDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Suppress("unused")
@Module
object MemoryDatabaseModule {
    @Provides
    @Singleton
    @JvmStatic
    fun provideDB(context: Context): RoomDB {
        return Room
            .inMemoryDatabaseBuilder(context, RoomDB::class.java)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideSeries(db: RoomDB) = db.series()

    @Provides
    @Singleton
    @JvmStatic
    fun provideUser(db: RoomDB) = db.user()
}
