package com.chesire.malime.injection.modules

import android.arch.persistence.room.Room
import android.content.Context
import com.chesire.malime.core.room.MalimeDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * For now this is just providing a different database file, need to correctly mock it out.
 */
@Suppress("unused")
@Module
internal class MockDatabaseModule {
    @Provides
    fun provideDatabase(context: Context): MalimeDatabase {
        return Room
            .databaseBuilder(context, MalimeDatabase::class.java, "malimetestdatabase.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(db: MalimeDatabase) = db.malimeDao()
}