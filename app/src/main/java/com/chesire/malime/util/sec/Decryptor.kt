package com.chesire.malime.util.sec

import java.nio.charset.Charset
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

// this cipher should work, will need to test < API23
private const val transformation = "AES/GCM/NoPadding"
private const val androidKeyStore = "AndroidKeyStore"

class Decryptor {
    private val keyStore = KeyStore.getInstance(androidKeyStore)

    init {
        keyStore.load(null)
    }

    fun decryptData(alias: String, encryptedData: ByteArray, encryptionIv: ByteArray): String {
        val spec = GCMParameterSpec(128, encryptionIv)
        val cipher = Cipher.getInstance(transformation).apply {
            init(Cipher.DECRYPT_MODE, getSecretKey(alias), spec)
        }

        return String(cipher.doFinal(encryptedData), Charset.forName("UTF-8"))
    }

    private fun getSecretKey(alias: String): SecretKey {
        return (keyStore.getEntry(alias, null) as KeyStore.SecretKeyEntry).secretKey
    }
}