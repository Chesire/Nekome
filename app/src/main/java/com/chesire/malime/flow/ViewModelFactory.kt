package com.chesire.malime.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = requireNotNull(creators[modelClass] ?: creators
            .asIterable()
            .firstOrNull { modelClass.isAssignableFrom(it.key) }
            ?.value
        ) {
            "unknown model class $modelClass"
        }
        return creator.get() as T
    }
}
