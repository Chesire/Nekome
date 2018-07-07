package com.chesire.malime.kitsu.api

import android.content.Context
import android.util.Base64
import com.chesire.malime.core.api.Authorizer
import com.chesire.malime.core.models.AuthModel
import com.chesire.malime.core.sec.Decryptor
import com.chesire.malime.core.sec.Encryptor
import com.google.gson.Gson

private const val authPrefFile: String = "malime_kitsu_auth_pref"
private const val authAlias: String = "kitsu_private_auth"
private const val preferenceAuth: String = "pref_auth"
private const val preferenceUser: String = "pref_user"

class KitsuAuthorizer(context: Context) : Authorizer<Int> {
    private val pref = context.getSharedPreferences(authPrefFile, Context.MODE_PRIVATE)
    private val encryptor = Encryptor(context.applicationContext)
    private val decryptor = Decryptor()

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

    override fun storeUser(user: Int) {
        pref.edit()
            .putInt(preferenceUser, user)
            .apply()
    }

    override fun retrieveUser(): Int {
        return pref.getInt(preferenceUser, 0)
    }

    override fun clear() {
        pref.edit()
            .remove(preferenceAuth)
            .remove(preferenceUser)
            .apply()
    }
}