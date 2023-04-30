package com.chesire.nekome.injection

import android.content.Context
import com.chesire.nekome.database.RoomDB
import com.chesire.nekome.database.dao.SeriesDao
import com.chesire.nekome.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger [Module] for the [com.chesire.nekome.database] package.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides the build [RoomDB].
     */
    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context) = RoomDB.build(context)

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
