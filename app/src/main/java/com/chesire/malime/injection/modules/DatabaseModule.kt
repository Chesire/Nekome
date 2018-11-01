package com.chesire.malime.injection.modules

import androidx.room.Room
import android.content.Context
import com.chesire.malime.core.room.MalimeDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Suppress("unused")
@Module
internal class DatabaseModule {
    @Provides
    fun provideDatabase(context: Context): MalimeDatabase {
        return Room
            .databaseBuilder(context, MalimeDatabase::class.java, "malimedatabase.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideDao(db: MalimeDatabase) = db.malimeDao()
}
