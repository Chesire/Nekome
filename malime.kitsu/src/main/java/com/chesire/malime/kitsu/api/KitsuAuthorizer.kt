package com.chesire.malime.kitsu.api

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.chesire.malime.core.PreferenceProvider
import com.chesire.malime.core.api.Authorizer
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.core.sec.Decryptor
import com.chesire.malime.core.sec.Encryptor
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

private const val AUTH_PREF_FILE = "malime_kitsu_auth_pref"
private const val AUTH_ALIAS = "kitsu_private_auth"
private const val PREFERENCE_AUTH = "pref_auth"
private const val PREFERENCE_USER = "pref_user"

@Singleton
class KitsuAuthorizer @Inject constructor(
    context: Context,
    prefProvider: PreferenceProvider,
    private val encryptor: Encryptor,
    private val decryptor: Decryptor
) : Authorizer<Int> {
    private val pref: SharedPreferences = prefProvider.getPreferencesFor(
        context,
        AUTH_PREF_FILE,
        Context.MODE_PRIVATE
    )
    private var cachedModel = getEmptyAuthModel()
    private var cachedUser = -1

    override fun storeAuthDetails(model: AuthModel) {
        cachedModel = model

        val encrypted = encryptor.encryptText(AUTH_ALIAS, Gson().toJson(model))
        pref.edit()
            .putString(PREFERENCE_AUTH, Base64.encodeToString(encrypted, Base64.DEFAULT))
            .apply()
    }

    override fun retrieveAuthDetails(): AuthModel {
        if (cachedModel.authToken.isBlank()) {
            val auth = pref.getString(PREFERENCE_AUTH, "")
            cachedModel = if (auth.isNotBlank()) {
                val decrypt = decryptor.decryptData(AUTH_ALIAS, Base64.decode(auth, Base64.DEFAULT))
                Gson().fromJson(decrypt, AuthModel::class.java)
            } else {
                getEmptyAuthModel()
            }
        }

        return cachedModel
    }

    override fun isDefaultUser(user: Any?) = if (user is Int) user == -1 else false

    override fun storeUser(user: Int) {
        cachedUser = user

        pref.edit()
            .putInt(PREFERENCE_USER, user)
            .apply()
    }

    override fun retrieveUser(): Int {
        if (cachedUser == -1) {
            cachedUser = pref.getInt(PREFERENCE_USER, -1)
        }

        return cachedUser
    }

    override fun clear() {
        cachedModel = getEmptyAuthModel()
        cachedUser = -1

        pref.edit()
            .remove(PREFERENCE_AUTH)
            .remove(PREFERENCE_USER)
            .apply()
    }

    private fun getEmptyAuthModel() = AuthModel("", "", 0, "")
}
