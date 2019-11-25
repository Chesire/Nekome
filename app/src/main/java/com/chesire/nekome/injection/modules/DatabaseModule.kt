package com.chesire.nekome.injection.modules

import android.content.Context
import com.chesire.nekome.database.RoomDB
import com.chesire.nekome.database.dao.SeriesDao
import com.chesire.nekome.database.dao.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Dagger [Module] for the [com.chesire.nekome.database] package.
 */
@Suppress("unused")
@Module
object DatabaseModule {
    /**
     * Provides the build [RoomDB].
     */
    @Provides
    @Singleton
    fun provideDB(context: Context) = RoomDB.build(context)

    /**
     * Provides the [SeriesDao] table from [RoomDB].
     */
    @Provides
    @Singleton
    fun provideSeries(db: RoomDB) = db.series()

    /**
     * Provides the [UserDao] table from [RoomDB].
     */
    @Provides
    @Singleton
    fun provideUser(db: RoomDB) = db.user()
}
