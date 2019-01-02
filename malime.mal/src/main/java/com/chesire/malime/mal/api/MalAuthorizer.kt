package com.chesire.malime.mal.api

import android.content.Context
import android.util.Base64
import com.chesire.malime.core.api.Authorizer
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.core.sec.Decryptor
import com.chesire.malime.core.sec.Encryptor
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

private const val AUTH_PREF_FILE = "malime_mal_auth_pref"
private const val AUTH_ALIAS = "mal_private_auth"
private const val PREFERENCE_AUTH = "pref_auth"
private const val PREFERENCE_USER = "pref_user"

// This should perform some caching on the credentials
@Singleton
class MalAuthorizer @Inject constructor(context: Context) : Authorizer<String> {
    private val pref = context.getSharedPreferences(AUTH_PREF_FILE, Context.MODE_PRIVATE)
    private val encryptor by lazy { Encryptor(context.applicationContext) }
    private val decryptor by lazy { Decryptor() }

    override fun storeAuthDetails(model: AuthModel) {
        val encrypted = encryptor.encryptText(AUTH_ALIAS, Gson().toJson(model))
        pref.edit()
            .putString(PREFERENCE_AUTH, Base64.encodeToString(encrypted, Base64.DEFAULT))
            .apply()
    }

    override fun retrieveAuthDetails(): AuthModel {
        val auth = pref.getString(PREFERENCE_AUTH, "")
        return if (auth.isNotBlank()) {
            val decrypted = decryptor.decryptData(AUTH_ALIAS, Base64.decode(auth, Base64.DEFAULT))
            Gson().fromJson(decrypted, AuthModel::class.java)
        } else {
            AuthModel("", "", 0, "")
        }
    }

    override fun isDefaultUser(user: Any?) = (user as? String)?.isBlank() ?: false

    override fun storeUser(user: String) {
        pref.edit()
            .putString(PREFERENCE_USER, user)
            .apply()
    }

    override fun retrieveUser() = pref.getString(PREFERENCE_USER, "")

    override fun clear() {
        pref.edit()
            .remove(PREFERENCE_AUTH)
            .remove(PREFERENCE_USER)
            .apply()
    }
}
