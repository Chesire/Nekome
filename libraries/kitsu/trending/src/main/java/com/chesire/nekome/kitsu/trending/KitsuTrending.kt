package com.chesire.nekome.kitsu.trending

import com.chesire.nekome.core.Resource
import com.chesire.nekome.kitsu.api.intermediaries.SeriesItem
import com.chesire.nekome.trending.api.TrendingApi
import com.chesire.nekome.trending.api.TrendingEntity
import com.chesire.nekome.trending.api.TrendingEntityMapper
import retrofit2.Response
import javax.inject.Inject
import kotlin.reflect.KSuspendFunction0

class KitsuTrending @Inject constructor(
    private val trendingService: KitsuTrendingService,
    private val mapper: TrendingEntityMapper<SeriesItem>
) : TrendingApi {

    override suspend fun trendingAnime(): Resource<List<TrendingEntity>> {
        try {
            return executeTrendingRequest(trendingService::getTrendingAnimeAsync)
        } catch (ex: Exception) {
            //ex.parse()
            error("")
        }
    }

    override suspend fun trendingManga(): Resource<List<TrendingEntity>> {
        try {
            return executeTrendingRequest(trendingService::getTrendingMangaAsync)
        } catch (ex: Exception) {
            //ex.parse()
            error("")
        }
    }

    private suspend fun executeTrendingRequest(
        execute: KSuspendFunction0<Response<TrendingData>>
    ): Resource<List<TrendingEntity>>{
        val result = execute()
        if (result.isSuccessful) {
            val body = result.body()
            body?.let {
                return Resource.Success(body.data.map { mapper.mapToTrendingEntity(it) })
            }
        }

        error("")
    }
}
