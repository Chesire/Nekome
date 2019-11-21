package com.chesire.malime.injection.modules

import android.content.Context
import com.chesire.malime.database.RoomDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Suppress("unused")
@Module
object MemoryDatabaseModule {
    @Provides
    @Singleton
    fun provideDB(context: Context): RoomDB = RoomDB.buildMemory(context)

    @Provides
    @Singleton
    fun provideSeries(db: RoomDB) = db.series()

    @Provides
    @Singleton
    fun provideUser(db: RoomDB) = db.user()
}
