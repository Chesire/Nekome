package com.chesire.nekome.encryption

import android.content.Context
import com.thz.keystorehelper.KeyStoreManager

/**
 * Used to encrypt and decrypt strings using the [KeyStoreManager].
 */
class KeystoreEncryption(context: Context) : Cryption {

    init {
        KeyStoreManager.init(context)
    }

    override fun encrypt(text: String, alias: String): String =
        KeyStoreManager.encryptData(text, alias)

    override fun decrypt(text: String, alias: String): String =
        KeyStoreManager.decryptData(text, alias)
}
