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
    fun providesAuthorizer(): Authorizer<*> {
        return (object : Authorizer<Int> {
            override fun storeAuthDetails(model: AuthModel) {
                TODO("not implemented")
            }

            override fun retrieveAuthDetails(): AuthModel {
                TODO("not implemented")
                // return AuthModel("", "", 0, "")
            }

            override fun isDefaultUser(user: Any?): Boolean {
                TODO("not implemented")
                // return if (user is Int) user == -1 else false
            }

            override fun storeUser(user: Int) {
                TODO("not implemented")
            }

            override fun retrieveUser(): Int {
                TODO("not implemented")
                // return 1
            }

            override fun clear() {
                TODO("not implemented")
            }
        })
    }
}