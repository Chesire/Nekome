package com.chesire.malime.core.sec

import java.nio.charset.StandardCharsets
import java.security.KeyStore
import java.security.PrivateKey
import javax.crypto.Cipher
import javax.inject.Inject

private const val transformation = "RSA/ECB/PKCS1Padding"
private const val androidKeyStore = "AndroidKeyStore"

class Decryptor @Inject constructor() {
    private val keyStore = KeyStore.getInstance(androidKeyStore)

    init {
        keyStore.load(null)
    }

    fun decryptData(alias: String, encryptedData: ByteArray): String {
        val secretKey = getSecretKey(alias) ?: return ""
        val cipher = Cipher.getInstance(transformation).apply {
            init(Cipher.DECRYPT_MODE, secretKey)
        }

        return String(cipher.doFinal(encryptedData), StandardCharsets.UTF_8)
    }

    private fun getSecretKey(alias: String): PrivateKey? {
        return keyStore.getKey(alias, null) as PrivateKey?
    }
}