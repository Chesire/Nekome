package com.chesire.malime.injection.modules

import androidx.room.Room
import android.content.Context
import com.chesire.malime.OpenForTesting
import com.chesire.malime.core.room.MalimeDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Suppress("unused")
@OpenForTesting
@Module
class MockDatabaseModule {
    @Provides
    fun provideDatabase(context: Context): MalimeDatabase {
        return Room
            .inMemoryDatabaseBuilder(context, MalimeDatabase::class.java)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(db: MalimeDatabase) = db.malimeDao()
}
