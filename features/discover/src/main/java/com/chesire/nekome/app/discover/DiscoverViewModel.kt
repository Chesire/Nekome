package com.chesire.nekome.app.discover

import androidx.lifecycle.ViewModel
import com.chesire.nekome.app.discover.domain.DiscoverDomainMapper
import com.chesire.nekome.datasource.trending.remote.TrendingApi
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel to aid with performing series discovery.
 */
@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val trending: TrendingApi,
    private val mapper: DiscoverDomainMapper
) : ViewModel()
