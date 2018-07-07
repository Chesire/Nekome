package com.chesire.malime.injection

import android.arch.persistence.room.Room
import android.content.Context
import com.chesire.malime.core.api.MalimeApi
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.core.repositories.Authorization
import com.chesire.malime.core.room.MalimeDao
import com.chesire.malime.core.room.MalimeDatabase
import com.chesire.malime.kitsu.api.KitsuAuthorizer
import com.chesire.malime.kitsu.api.KitsuManager
import com.chesire.malime.util.updateservice.PeriodicUpdateService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ServiceModule(private val service: PeriodicUpdateService) {
    @Provides
    fun providePeriodicUpdateService(): PeriodicUpdateService {
        return service
    }

    @Provides
    fun provideContext(service: PeriodicUpdateService): Context {
        return service.applicationContext
    }

    @Singleton
    @Provides
    fun provideDatabase(service: PeriodicUpdateService): MalimeDatabase {
        return Room
            .databaseBuilder(service, MalimeDatabase::class.java, "malimedatabase.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideDao(db: MalimeDatabase): MalimeDao {
        return db.malimeDao()
    }

    @Singleton
    @Provides
    fun provideAuthorization(
        kitsuAuthorizer: KitsuAuthorizer
        // malAuthorizer: MalAuthorizer
    ): Authorization {
        return Authorization(
            mapOf(
                Pair(SupportedService.Kitsu, kitsuAuthorizer)
                // Pair(SupportedService.MyAnimeList, malAuthorizer)
            )
        )
    }

    // for now we can just return the KitsuManager, as we don't support anything else yet
    @Provides
    fun providesMalimeApi(manager: KitsuManager): MalimeApi {
        return manager
    }
}