package com.chesire.nekome.injection

import android.content.Context
import com.chesire.nekome.database.RoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object MemoryDatabaseModule {
    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context): RoomDB = RoomDB.buildMemory(context)

    @Provides
    @Singleton
    fun provideSeries(db: RoomDB) = db.series()

    @Provides
    @Singleton
    fun provideUser(db: RoomDB) = db.user()
}
