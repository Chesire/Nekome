package com.chesire.malime.mocks

import com.chesire.malime.core.api.Authorizer
import com.chesire.malime.core.models.AuthModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockAuthorizer @Inject constructor() : Authorizer<Int> {
    override fun storeAuthDetails(model: AuthModel) {
    }

    override fun retrieveAuthDetails(): AuthModel {
        return getEmptyAuthModel()
    }

    override fun isDefaultUser(user: Any?): Boolean {
        return if (user is Int) user == -1 else false
    }

    override fun storeUser(user: Int) {
    }

    override fun retrieveUser(): Int {
        return 1
    }

    override fun clear() {
    }

    private fun getEmptyAuthModel(): AuthModel {
        return AuthModel("", "", 0, "")
    }
}