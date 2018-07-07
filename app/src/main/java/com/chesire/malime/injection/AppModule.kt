package com.chesire.malime.injection

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.core.repositories.Authorization
import com.chesire.malime.core.room.MalimeDao
import com.chesire.malime.core.room.MalimeDatabase
import com.chesire.malime.kitsu.api.KitsuAuthorizer
import dagger.Module
import dagger.Provides

@Suppress("unused")
@Module(includes = [(ViewModelModule::class)])
internal class AppModule {
    @Provides
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    fun provideDatabase(application: Application): MalimeDatabase {
        return Room
            .databaseBuilder(
                application,
                MalimeDatabase::class.java,
                "malimedatabase.db"
            )
            .build()
    }

    @Provides
    fun provideDao(db: MalimeDatabase): MalimeDao {
        return db.malimeDao()
    }

    @Provides
    fun provideAuthorization(
        kitsuAuthorizer: KitsuAuthorizer
        //malAuthorizer: MalAuthorizer
    ): Authorization {
        return Authorization(
            mapOf(
                Pair(SupportedService.Kitsu, kitsuAuthorizer)
                //Pair(SupportedService.MyAnimeList, malAuthorizer)
            )
        )
    }
}