package com.chesire.malime.injection.modules

import android.arch.persistence.room.Room
import android.content.Context
import com.chesire.malime.core.room.MalimeDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Suppress("unused")
@Module
internal class MockDatabaseModule {
    @Provides
    fun provideDatabase(context: Context): MalimeDatabase {
        return Room.inMemoryDatabaseBuilder(context, MalimeDatabase::class.java).build()
    }

    @Singleton
    @Provides
    fun provideDao(db: MalimeDatabase) = db.malimeDao()
}