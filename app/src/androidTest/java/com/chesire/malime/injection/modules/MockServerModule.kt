package com.chesire.malime.injection.modules

import com.chesire.malime.OpenForTesting
import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.core.api.Authorizer
import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.core.repositories.Authorization
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
    fun providesAuthorization(authorizer: Authorizer<*>): Authorization {
        return Authorization(mapOf(SupportedService.Unknown to authorizer))
    }

    @Provides
    fun providesAuthApi(): AuthApi {
        return (object : AuthApi {
            override fun login(username: String, password: String): Single<AuthModel> {
                TODO("injected mock should be used")
            }

            override fun getNewAuthToken(refreshToken: String): Single<AuthModel> {
                TODO("injected mock should be used")
            }

            override fun getUserId(): Single<Int> {
                TODO("injected mock should be used")
            }
        })
    }

    @Provides
    fun providesMalimeApi(mockApi: MockLibraryApi): LibraryApi = mockApi

    @Provides
    fun providesSearchApi(mockApi: MockSearchApi): SearchApi = mockApi

    @Provides
    fun providesAuthorizer(): Authorizer<*> {
        return (object : Authorizer<Int> {
            override fun storeAuthDetails(model: AuthModel) {
                TODO("injected mock should be used")
            }

            override fun retrieveAuthDetails(): AuthModel {
                // return AuthModel("", "", 0, "")
                TODO("injected mock should be used")
            }

            override fun isDefaultUser(user: Any?): Boolean {
                // return if (user is Int) user == -1 else false
                TODO("injected mock should be used")
            }

            override fun storeUser(user: Int) {
                TODO("injected mock should be used")
            }

            override fun retrieveUser(): Int {
                // return 1
                TODO("injected mock should be used")
            }

            override fun clear() {
                TODO("injected mock should be used")
            }
        })
    }
}