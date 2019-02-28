package com.chesire.malime.injection.modules

import android.content.Context
import androidx.room.Room
import com.chesire.malime.db.RoomDB
import dagger.Module
import dagger.Provides
import dagger.Reusable

@Suppress("unused")
@Module
object DatabaseModule {
    @Provides
    @JvmStatic
    fun provideDB(context: Context): RoomDB {
        return Room
            .databaseBuilder(context, RoomDB::class.java, "malime_database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Reusable
    @JvmStatic
    fun provideSeries(db: RoomDB) = db.series()

    @Provides
    @Reusable
    @JvmStatic
    fun provideUser(db: RoomDB) = db.user()
}
