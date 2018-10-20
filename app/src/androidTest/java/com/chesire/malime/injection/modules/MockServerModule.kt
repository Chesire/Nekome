package com.chesire.malime.injection.modules

import com.chesire.malime.OpenForTesting
import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.core.api.Authorizer
import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.core.repositories.Authorization
import com.chesire.malime.mocks.MockAuthorizer
import com.chesire.malime.mocks.MockLibraryApi
import com.chesire.malime.mocks.MockSearchApi
import dagger.Module
import dagger.Provides
import io.reactivex.Single
import javax.inject.Singleton

@Suppress("unused")
@OpenForTesting
@Module
class MockServerModule {
    @Singleton
    @Provides
    fun providesAuthorization(authorizer: MockAuthorizer): Authorization {
        return Authorization(mapOf(SupportedService.Unknown to authorizer))
    }

    @Provides
    fun providesAuthApi(): AuthApi {
        return (object : AuthApi {
            override fun login(username: String, password: String): Single<AuthModel> {
                TODO("not implemented")
            }

            override fun getNewAuthToken(refreshToken: String): Single<AuthModel> {
                TODO("not implemented")
            }

            override fun getUserId(): Single<Int> {
                TODO("not implemented")
            }
        })
    }

    @Provides
    fun providesMalimeApi(mockApi: MockLibraryApi): LibraryApi = mockApi

    @Provides
    fun providesSearchApi(mockApi: MockSearchApi): SearchApi = mockApi

    @Provides
    fun providesAuthorizer(authorizer: MockAuthorizer): Authorizer<*> = authorizer
}