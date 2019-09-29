package com.chesire.malime.app.discover

import androidx.lifecycle.ViewModel
import com.chesire.malime.server.api.SearchApi
import javax.inject.Inject

/**
 * ViewModel to aid with performing series discovery.
 */
class DiscoverViewModel @Inject constructor(
    private val search: SearchApi
) : ViewModel()
