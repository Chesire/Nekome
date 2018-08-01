package com.chesire.malime.injection.modules

import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.core.repositories.Authorization
import com.chesire.malime.kitsu.api.KitsuAuthorizer
import com.chesire.malime.kitsu.api.KitsuManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Suppress("unused")
@Module
internal class ServerModule {
    @Singleton
    @Provides
    fun provideAuthorization(
        kitsuAuthorizer: KitsuAuthorizer
        // malAuthorizer: MalAuthorizer
    ): Authorization {
        return Authorization(
            mapOf(
                SupportedService.Kitsu to kitsuAuthorizer
                // Pair(SupportedService.MyAnimeList, malAuthorizer)
            )
        )
    }

    // for now we can just return the KitsuManager, as we don't support anything else yet
    @Provides
    fun providesAuthApi(manager: KitsuManager): AuthApi = manager

    // for now we can just return the KitsuManager, as we don't support anything else yet
    @Provides
    fun providesMalimeApi(manager: KitsuManager): LibraryApi = manager

    // for now we can just return the KitsuManager, as we don't support anything else yet
    @Provides
    fun providesSearchApi(manager: KitsuManager): SearchApi = manager
}