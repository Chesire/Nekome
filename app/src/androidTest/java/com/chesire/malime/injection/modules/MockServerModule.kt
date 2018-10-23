package com.chesire.malime.injection.modules

import com.chesire.malime.OpenForTesting
import com.chesire.malime.core.api.AuthApi
import com.chesire.malime.core.api.Authorizer
import com.chesire.malime.core.api.LibraryApi
import com.chesire.malime.core.api.SearchApi
import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.flags.SupportedService
import com.chesire.malime.core.flags.UserSeriesStatus
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.core.models.MalimeModel
import com.chesire.malime.core.repositories.Authorization
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
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
        return object : AuthApi {
            override fun login(username: String, password: String): Single<AuthModel> {
                TODO("injected mock should be used")
            }

            override fun getNewAuthToken(refreshToken: String): Single<AuthModel> {
                TODO("injected mock should be used")
            }

            override fun getUserId(): Single<Int> {
                TODO("injected mock should be used")
            }
        }
    }

    @Provides
    fun providesLibraryApi(): LibraryApi {
        return object : LibraryApi {
            override fun addItem(item: MalimeModel): Single<MalimeModel> {
                TODO("injected mock should be used")
            }

            override fun deleteItem(item: MalimeModel): Single<MalimeModel> {
                TODO("injected mock should be used")
            }

            override fun getUserLibrary(): Observable<List<MalimeModel>> {
                TODO("injected mock should be used")
            }

            override fun updateItem(
                item: MalimeModel,
                newProgress: Int,
                newStatus: UserSeriesStatus
            ): Single<MalimeModel> {
                TODO("injected mock should be used")
            }
        }
    }

    @Provides
    fun providesSearchApi(): SearchApi {
        return object : SearchApi {
            override fun searchForSeriesWith(
                title: String,
                type: ItemType
            ): Single<List<MalimeModel>> {
                TODO("injected mock should be used")
            }
        }
    }

    @Provides
    fun providesAuthorizer(): Authorizer<*> {
        return object : Authorizer<Int> {
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
        }
    }
}