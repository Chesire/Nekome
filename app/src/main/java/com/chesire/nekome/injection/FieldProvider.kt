package com.chesire.nekome.injection

import com.chesire.nekome.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import java.lang.reflect.Field

/**
 * Dagger [Module] to provide the application [Field].
 */
@Module
@InstallIn(FragmentComponent::class)
object FieldProvider {

    /**
     * Provides the string fields.
     * For use to show the open source libraries used.
     */
    @Provides
    fun provideFields(): Array<Field> = R.string::class.java.fields
}
