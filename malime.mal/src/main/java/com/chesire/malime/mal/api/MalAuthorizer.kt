package com.chesire.malime.mal.api

import android.content.Context
import android.util.Base64
import com.chesire.malime.core.api.Authorizer
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.core.sec.Decryptor
import com.chesire.malime.core.sec.Encryptor
import com.google.gson.Gson
import javax.inject.Inject

private const val authPrefFile: String = "malime_mal_auth_pref"
private const val authAlias: String = "mal_private_auth"
private const val preferenceAuth: String = "pref_auth"
private const val preferenceUser: String = "pref_user"

class MalAuthorizer @Inject constructor(context: Context) : Authorizer<String> {
    private val pref = context.getSharedPreferences(authPrefFile, Context.MODE_PRIVATE)
    private val encryptor by lazy { Encryptor(context.applicationContext) }
    private val decryptor by lazy { Decryptor() }

    override fun storeAuthDetails(model: AuthModel) {
        val encrypted = encryptor.encryptText(authAlias, Gson().toJson(model))
        pref.edit()
            .putString(preferenceAuth, Base64.encodeToString(encrypted, Base64.DEFAULT))
            .apply()
    }

    override fun retrieveAuthDetails(): AuthModel {
        val auth = pref.getString(preferenceAuth, "")
        return if (auth.isNotBlank()) {
            val decrypted = decryptor.decryptData(authAlias, Base64.decode(auth, Base64.DEFAULT))
            Gson().fromJson(decrypted, AuthModel::class.java)
        } else {
            AuthModel("", "", 0, "")
        }
    }

    override fun isDefaultUser(user: Any?): Boolean {
        return (user as? String)?.isBlank() ?: false
    }

    override fun storeUser(user: String) {
        pref.edit()
            .putString(preferenceUser, user)
            .apply()
    }

    override fun retrieveUser(): String {
        return pref.getString(preferenceUser, "")
    }

    override fun clear() {
        pref.edit()
            .remove(preferenceAuth)
            .remove(preferenceUser)
            .apply()
    }
}