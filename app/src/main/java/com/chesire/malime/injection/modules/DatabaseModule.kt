package com.chesire.malime.injection.modules

import android.content.Context
import androidx.room.Room
import com.chesire.malime.database.RoomDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Suppress("unused")
@Module
object DatabaseModule {
    @Provides
    @Singleton
    @JvmStatic
    fun provideDB(context: Context) = RoomDB.build(context)

    @Provides
    @Singleton
    @JvmStatic
    fun provideSeries(db: RoomDB) = db.series()

    @Provides
    @Singleton
    @JvmStatic
    fun provideUser(db: RoomDB) = db.user()
}
