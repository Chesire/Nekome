package com.chesire.nekome.injection

import android.content.Context
import androidx.room.Room
import com.chesire.nekome.database.RoomDB
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object MemoryDatabaseModule {

    @Provides
    @Singleton
    fun provideDB(@ApplicationContext context: Context): RoomDB =
        Room.inMemoryDatabaseBuilder(context, RoomDB::class.java)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideSeries(db: RoomDB) = db.series()

    @Provides
    @Singleton
    fun provideUser(db: RoomDB) = db.user()
}
