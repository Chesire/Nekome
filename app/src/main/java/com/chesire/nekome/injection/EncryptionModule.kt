package com.chesire.nekome.injection

import android.content.Context
import com.chesire.nekome.encryption.Cryption
import com.chesire.nekome.encryption.KeystoreEncryption
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * Dagger [Module] for providing [Cryption] to parts of the application.
 */
@Module
@InstallIn(ApplicationComponent::class)
object EncryptionModule {
    @Provides
    fun providesCryption(@ApplicationContext context: Context): Cryption =
        KeystoreEncryption(context)
}
