package com.chesire.malime.core.models

import com.chesire.malime.core.flags.ItemType
import com.chesire.malime.core.flags.SeriesStatus
import com.chesire.malime.core.flags.Subtype
import com.chesire.malime.core.flags.UserSeriesStatus
import org.junit.Assert
import org.junit.Test

class MalimeModelTests {
    @Test
    fun `total series length of -1 returns unknown string`() {
        val testObject = generateTestModel()
        testObject.totalLength = -1

        Assert.assertEquals("??", testObject.getTotalSeriesLength())
    }

    @Test
    fun `total series length of 0 returns unknown string`() {
        val testObject = generateTestModel()
        testObject.totalLength = 0

        Assert.assertEquals("??", testObject.getTotalSeriesLength())
    }

    @Test
    fun `total series length of 1 returns expected length`() {
        val testObject = generateTestModel()
        testObject.totalLength = 12

        Assert.assertEquals("12", testObject.getTotalSeriesLength())
    }

    @Test
    fun `-1 out of 10 progress gets progress percent of 0%`() {
        val testObject = generateTestModel()
        testObject.progress = -1
        testObject.totalLength = 10

        Assert.assertEquals(0, testObject.getSeriesProgressPercent())
    }

    @Test
    fun `0 out of 10 progress gets progress percent of 0%`() {
        val testObject = generateTestModel()
        testObject.progress = 0
        testObject.totalLength = 10

        Assert.assertEquals(0, testObject.getSeriesProgressPercent())
    }

    @Test
    fun `1 out of 10 progress gets progress percent of 10%`() {
        val testObject = generateTestModel()
        testObject.progress = 1
        testObject.totalLength = 10

        Assert.assertEquals(10, testObject.getSeriesProgressPercent())
    }

    @Test
    fun `100 out of 100 progress gets progress percent of 100%`() {
        val testObject = generateTestModel()
        testObject.progress = 100
        testObject.totalLength = 100

        Assert.assertEquals(100, testObject.getSeriesProgressPercent())
    }

    @Test
    fun `0 out of unknown progress gets progress percent of 0%`() {
        val testObject = generateTestModel()
        testObject.progress = 0
        testObject.totalLength = 0

        Assert.assertEquals(0, testObject.getSeriesProgressPercent())
    }

    @Test
    fun `1 out of unknown progress gets progress percent of 50%`() {
        val testObject = generateTestModel()
        testObject.progress = 1
        testObject.totalLength = 0

        Assert.assertEquals(50, testObject.getSeriesProgressPercent())
    }

    @Test
    fun `cannot decrease progress if value is 0`() {
        val testObject = generateTestModel()
        testObject.progress = 0

        Assert.assertFalse(testObject.canDecreaseProgress())
    }

    @Test
    fun `can decrease progress if value is 1`() {
        val testObject = generateTestModel()
        testObject.progress = 1

        Assert.assertTrue(testObject.canDecreaseProgress())
    }

    @Test
    fun `can decrease progress multiple times till value is 0`() {
        val testObject = generateTestModel()
        testObject.progress = 3

        Assert.assertTrue(testObject.canDecreaseProgress())
        testObject.progress = 2
        Assert.assertTrue(testObject.canDecreaseProgress())
        testObject.progress = 0
        Assert.assertFalse(testObject.canDecreaseProgress())
    }

    @Test
    fun `cannot increase progress if value is total length`() {
        val testObject = generateTestModel()
        testObject.totalLength = 12
        testObject.progress = 12

        Assert.assertFalse(testObject.canIncreaseProgress())
    }

    @Test
    fun `can increase progress if value is 0`() {
        val testObject = generateTestModel()
        testObject.totalLength = 12
        testObject.progress = 0

        Assert.assertTrue(testObject.canIncreaseProgress())
    }

    @Test
    fun `can increase progress till value is total length`() {
        val testObject = generateTestModel()
        testObject.totalLength = 12
        testObject.progress = 0

        Assert.assertTrue(testObject.canIncreaseProgress())
        testObject.progress = 5
        Assert.assertTrue(testObject.canIncreaseProgress())
        testObject.progress = 12
        Assert.assertFalse(testObject.canIncreaseProgress())
    }

    private fun generateTestModel(): MalimeModel {
        return MalimeModel(
            0,
            0,
            ItemType.Unknown,
            Subtype.Unknown,
            "",
            "",
            SeriesStatus.Unknown,
            UserSeriesStatus.Unknown,
            0,
            0,
            "",
            "",
            false,
            "",
            ""
        )
    }
}