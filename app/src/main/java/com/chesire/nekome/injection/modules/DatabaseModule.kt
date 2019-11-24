package com.chesire.nekome.injection.modules

import android.content.Context
import com.chesire.nekome.database.RoomDB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Suppress("unused")
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDB(context: Context) = RoomDB.build(context)

    @Provides
    @Singleton
    fun provideSeries(db: RoomDB) = db.series()

    @Provides
    @Singleton
    fun provideUser(db: RoomDB) = db.user()
}
