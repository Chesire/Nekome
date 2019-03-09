package com.chesire.malime.flow.overview.anime

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.chesire.malime.IOContext
import com.chesire.malime.core.models.SeriesModel
import com.chesire.malime.repo.SeriesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AnimeViewModel @Inject constructor(
    private val repo: SeriesRepository,
    @IOContext private val ioContext: CoroutineContext
) : ViewModel() {

    private val job = Job()
    private val ioScope = CoroutineScope(job + ioContext)
    val animeSeries: LiveData<List<SeriesModel>>
        get() = repo.anime

    fun refresh() = ioScope.launch {
        repo.refreshAnime()
    }
}
