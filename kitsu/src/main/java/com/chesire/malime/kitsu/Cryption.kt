package com.chesire.malime.kitsu

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.security.KeyPairGeneratorSpec
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import dagger.Reusable
import java.math.BigInteger
import java.nio.charset.StandardCharsets
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.util.Calendar
import javax.crypto.Cipher
import javax.inject.Inject
import javax.security.auth.x500.X500Principal

private const val TRANSFORMATION = "RSA/ECB/PKCS1Padding"
private const val ALGORITHM = "RSA"
private const val ANDROID_KEY_STORE = "AndroidKeyStore"
private const val ALIAS = "kitsuPrivateAuth"

class Cryption @Inject constructor(context: Context) {
    private val decryption = Decryption()
    private val encryption = Encryption(context)

    fun encryptText(text: String) = encryption.encrypt(text)
    fun decryptData(encryptedData: ByteArray) = decryption.decrypt(encryptedData)
    fun base64Encrypt(input: ByteArray): String = Base64.encodeToString(input, Base64.DEFAULT)
    fun base64Decrypt(input: String): ByteArray = Base64.decode(input, Base64.DEFAULT)

    private inner class Decryption {
        private val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)

        init {
            keyStore.load(null)
        }

        fun decrypt(encryptedData: ByteArray): String {
            val secretKey = keyStore.getKey(ALIAS, null) as? PrivateKey? ?: return ""

            val cipher = Cipher.getInstance(TRANSFORMATION).apply {
                init(Cipher.DECRYPT_MODE, secretKey)
            }

            return String(cipher.doFinal(encryptedData), StandardCharsets.UTF_8)
        }
    }

    private inner class Encryption(context: Context) {
        private val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
        private val keySpec: KeyPairGeneratorSpec

        init {
            keyStore.load(null)

            val startDate = Calendar.getInstance()
            val endDate = Calendar.getInstance()
            endDate.add(Calendar.YEAR, 20)

            @Suppress("DEPRECATION")
            keySpec = KeyPairGeneratorSpec
                .Builder(context)
                .setAlias(ALIAS)
                .setSerialNumber(BigInteger.ONE)
                .setSubject(X500Principal("CN=$ALIAS CA Certificate"))
                .setStartDate(startDate.time)
                .setEndDate(endDate.time)
                .build()
        }

        fun encrypt(text: String): ByteArray {
            val keyPair = createAndroidKeyStoreAsymmetricKey()
            val cipher = Cipher.getInstance(TRANSFORMATION).apply {
                init(Cipher.ENCRYPT_MODE, keyPair.public)
            }

            return cipher.doFinal(text.toByteArray(StandardCharsets.UTF_8))
        }

        private fun createAndroidKeyStoreAsymmetricKey(): KeyPair {
            val generator = KeyPairGenerator.getInstance(ALGORITHM, ANDROID_KEY_STORE)

            if (SDK_INT >= Build.VERSION_CODES.M) {
                initGeneratorWithKeyGenParameterSpec(generator)
            } else {
                initGeneratorWithKeyPairGeneratorSpec(generator)
            }

            // Generates Key with given spec and saves it to the KeyStore
            return generator.generateKeyPair()
        }

        @TargetApi(Build.VERSION_CODES.M)
        private fun initGeneratorWithKeyGenParameterSpec(generator: KeyPairGenerator) {
            val builder = KeyGenParameterSpec
                .Builder(ALIAS, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_ECB)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)

            generator.initialize(builder.build())
        }

        private fun initGeneratorWithKeyPairGeneratorSpec(generator: KeyPairGenerator) {
            generator.initialize(keySpec)
        }
    }
}
