package com.chesire.nekome.app.search.search.ui

import com.chesire.nekome.core.flags.SeriesType
import com.chesire.nekome.core.flags.Subtype
import com.chesire.nekome.core.models.ImageModel
import com.chesire.nekome.core.preferences.SeriesPreferences
import com.chesire.nekome.core.preferences.flags.ImageQuality
import com.chesire.nekome.core.preferences.flags.TitleLanguage
import com.chesire.nekome.datasource.search.SearchDomain
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DomainMapperTest {

    private val seriesPreferences = mockk<SeriesPreferences>()
    private lateinit var mapper: DomainMapper
    private val initialDomain = SearchDomain(
        id = 0,
        type = SeriesType.Anime,
        synopsis = "Synopsis",
        canonicalTitle = "canonicalTitle",
        otherTitles = mapOf(
            TitleLanguage.English.key to "en",
            TitleLanguage.Japanese.key to "jp"
        ),
        subtype = Subtype.Movie,
        posterImage = ImageModel(
            tiny = ImageModel.ImageData(
                url = "tiny",
                width = 0,
                height = 0
            ),
            small = ImageModel.ImageData(
                url = "small",
                width = 0,
                height = 0
            ),
            medium = ImageModel.ImageData(
                url = "medium",
                width = 0,
                height = 0
            ),
            large = ImageModel.ImageData(
                url = "large",
                width = 0,
                height = 0
            )
        )
    )

    @Before
    fun setup() {
        clearAllMocks()

        every { seriesPreferences.imageQuality } returns flowOf(ImageQuality.Medium)
        every { seriesPreferences.titleLanguage } returns flowOf(TitleLanguage.Canonical)
        mapper = DomainMapper(seriesPreferences)
    }

    @Test
    fun `When toResultModels, Then list of ResultModel item is returned`() = runTest {
        val input = listOf(initialDomain)
        val expected = listOf(
            ResultModel(
                id = 0,
                type = SeriesType.Anime,
                synopsis = "Synopsis",
                title = "canonicalTitle",
                subtype = Subtype.Movie.name,
                posterImage = "medium",
                canTrack = true,
                isTracking = false
            )
        )

        val result = mapper.toResultModels(input, emptyList())

        assertEquals(expected, result)
    }

    @Test
    fun `Given user already tracks ID, When toResultModels, Then canTrack is false`() = runTest {
        val input = listOf(initialDomain)
        val expected = listOf(
            ResultModel(
                id = 0,
                type = SeriesType.Anime,
                synopsis = "Synopsis",
                title = "canonicalTitle",
                subtype = Subtype.Movie.name,
                posterImage = "medium",
                canTrack = false,
                isTracking = false
            )
        )

        val result = mapper.toResultModels(input, listOf(0))

        assertEquals(expected, result)
    }
}
